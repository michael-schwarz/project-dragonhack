SELECT Us.nickname, (Us.total_slack / Us.total_all * 1.0) AS total
FROM users Us, university Un
WHERE Us.nickname=?' AND Un.university_id=Us.university_id
ORDER BY total;