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
    ids = ['111']

    def get(self, device_id):
        if device_id in DeviceIDResource.ids:
            return {'user_id': 1}
        else:
            abort(404, "Requested device ID not found")


class UserNameResource(Resource):
    def get(self, name):
        pass

    def put(self, name):
        pass


class UserIDResource(Resource):
    def get(self, user_id):
        pass
