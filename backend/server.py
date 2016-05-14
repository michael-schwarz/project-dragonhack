from flask import Flask
from flask_restful import Resource, Api

import psycopg2
from psycopg2.extras import RealDictCursor

import resources
from resources import *

app = Flask(__name__)
api = Api(app)
db_conn = psycopg2.connect("dbname=LecturePotato user=postgres password=verysecure")
resources.cursor = db_conn.cursor(cursor_factory=RealDictCursor)

api.add_resource(HeartbeatResource, '/heartbeat/<int:user_id>')
api.add_resource(LeaderboardUniversityResource, '/leaderboard/university/<string:university_id>/<string:range>')
api.add_resource(LeaderboardUserResource, '/leaderboard/user/<string:user_id>/<string:range>')
api.add_resource(DeviceIDResource, '/device/<string:device_id>')
api.add_resource(UserNameResource, '/user/name/<string:name>')
api.add_resource(UserIDResource, '/user/id/<int:user_id>')

if __name__ == '__main__':
    app.run(debug=True)
