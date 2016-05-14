SELECT Us.nickname, (Us.day_slack / Us.day_all * 1.0) AS daily
FROM users Us, university Un
WHERE Us.nickname=?' AND Un.university_id=Us.university_id
ORDER BY daily;