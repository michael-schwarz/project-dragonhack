from flask_restful import Resource, abort, reqparse

user_ids = set([1])
cursor = None


class HeartbeatResource(Resource):
    put_parser = reqparse.RequestParser()
    put_parser.add_argument('lecture_time', type=int, help="Lecture time in seconds", required=True)
    put_parser.add_argument('slack_time', type=int, help="Slacking time in seconds", required=True)
    heartbeats = []

    def put(self, user_id):
        args = HeartbeatResource.put_parser.parse_args()
        if user_id not in user_ids:
            abort(412, message="Bad user id")
        if cursor is not None:
            cursor.execute("INSERT INTO heartbeats (user_id, lecture_time, slack_time) VALUES (%s, %s, %s)",
                           (user_id, args['lecture_time'], args['slack_time']))

    def get(self, user_id=None):
        if user_id not in user_ids:
            abort(412, message="Bad user id")
        cursor.execute("SELECT * FROM heartbeats")
        return [r for r in cursor.fetchall()]


class LeaderboardUserResource(Resource):
    def get(self, university_id, range='all'):
        if range != 'all':
            return {'error': '{} is not a valid range'.format(range)}
        return {'id': university_id, 'range': range}


class LeaderboardUniversityResource(Resource):
    def get(self, university_id, range='all'):
        if range != 'all':
            return {'error': '{} is not a valid range'.format(range)}
        return {'id': university_id, 'range': range}


class DeviceIDResource(Resource):
    def get(self, device_id):
        conn.execute("SELECT ")
        if device_id in DeviceIDResource.ids:
            return {'user_id': 1}
        else:
            abort(404, "Requested device ID not found")


class UserNameResource(Resource):
    put_parser = reqparse.RequestParser()
    put_parser.add_argument('device_id', type=str, help="Device ID string", required=True)
    put_parser.add_argument('university_id', type=int, help="University ID", required=True)

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
        cursor.execute("INSERT INTO users (nickname, device_id, university_id) VALUES (%s, %s, %s)",
                       (name, device_id, university_id))
        cursor.commit()


class UserIDResource(Resource):
    def get(self, user_id):
        pass
