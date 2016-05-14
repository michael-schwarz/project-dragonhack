SELECT Us.nickname, (Us.month_slack / Us.month_all * 1.0) AS monthly
FROM users Us, university Un
WHERE Us.nickname=?' AND Un.university_id=Us.university_id
ORDER BY monthly;