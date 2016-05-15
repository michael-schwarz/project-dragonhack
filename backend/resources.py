from flask_restful import Resource, abort, reqparse
from psycopg2.extras import RealDictCursor

user_ids = set([1])
db_conn = None
cursor = None


class HeartbeatResource(Resource):
    put_parser = reqparse.RequestParser()
    put_parser.add_argument('lecture_time', type=int, help="Lecture time in seconds", required=True)
    put_parser.add_argument('slack_time', type=int, help="Slacking time in seconds", required=True)
    heartbeats = []

    def put(self, user_id):
        args = HeartbeatResource.put_parser.parse_args()
        time_all = args['lecture_time']
        time_slack = args['slack_time']
        cursor.execute("INSERT INTO heartbeats (time_all, time_slack, user_id) VALUES (%s, %s, %s)",
                       (time_all, time_slack, user_id))
        cursor.execute("UPDATE users "
                       "SET "
                       "day_all = day_all + %s, "
                       "month_all = month_all + %s, "
                       "total_all = total_all + %s", (time_all, time_all, time_all))
        cursor.execute("UPDATE users "
                       "SET "
                       "day_slack = day_slack + %s, "
                       "month_slack = month_slack + %s, "
                       "total_slack = total_slack + %s", (time_slack, time_slack, time_slack))
        db_conn.commit()

    def get(self, user_id=None):
        if user_id is None:
            cursor.execute("SELECT * FROM heartbeats LIMIT 100")
            return {'data': cursor.fetchall()}
        else:
            cursor.execute("SELECT * FROM heartbeats WHERE user_id = %s LIMIT 100", (user_id,))
            res = cursor.fetchone()
            if res is not None:
                return res
            else:
                return dict()


class LeaderboardUserResource(Resource):
    def get(self, user_id, range='total'):
        if range not in {'day', 'month', 'total'}:
            abort(400, message="Range must be one of {day, month, total}")
        cursor.execute(
            "SELECT Us.nickname, cast(1.0*Us.{}_slack/Us.{}_all AS FLOAT) AS stat "
            "FROM users Us "
            "INNER JOIN university Un ON Us.university_id=Un.university_id "
            "WHERE Us.{}_all>0 "
            "ORDER BY stat"
            .format(range, range, range))
        return {'data': cursor.fetchall()}


class LeaderboardUniversityResource(Resource):
    def get(self, university_id, range='total'):
        if range not in {'day', 'month', 'total'}:
            abort(400, message="Range must be one of {day, month, total}")
        cursor.execute(
            "SELECT Us.nickname, cast(1.0*Us.{}_slack/Us.{}_all AS FLOAT) AS stat "
            "FROM users Us "
            "WHERE Us.{}_all>0 AND Us.university_id = %s "
            "ORDER BY stat"
                .format(range, range, range), (university_id, ))
        return {'data': cursor.fetchall()}


class DeviceIDResource(Resource):
    def get(self, device_id):
        cursor.execute("SELECT * FROM users WHERE device_id=%s", (device_id,))
        res = cursor.fetchone()
        if res is None:
            abort(404, message="Requested device ID not found")
        else:
            return res


class UserNameResource(Resource):
    put_parser = reqparse.RequestParser()
    put_parser.add_argument('device_id', type=str, help="Device UUID", required=True, location='json')
    put_parser.add_argument('university_id', type=int, help="University ID", required=True, location='json')

    def get(self, name):
        cursor.execute("SELECT * FROM users WHERE nickname=%s", (name,))
        if cursor.rowcount == 0:
            return dict(), 404
        return cursor.fetchone()

    def put(self, name):
        args = UserNameResource.put_parser.parse_args()
        device_id = args['device_id']
        university_id = args['university_id']
        cursor.execute("SELECT 1 FROM users WHERE nickname=%s", (name,))
        if cursor.fetchone() is not None:
            abort(412, message="Nickname {} already exists".format(name))
        cursor.execute("SELECT 1 FROM users WHERE device_id=%s", (device_id,))
        if cursor.fetchone() is not None:
            abort(412, message="Device UUID {} already exists".format(name))
        cursor.execute("INSERT INTO users (nickname, device_id, university_id) VALUES (%s, %s, %s)",
                       (name, device_id, university_id))
        db_conn.commit()


class UserIDResource(Resource):
    def get(self, user_id):
        cursor.execute("SELECT * FROM users WHERE user_id=%s", (user_id,))
        res = cursor.fetchone()
        if res is None:
            abort(404, message="User ID {} not found".format(user_id))
        else:
            return res
