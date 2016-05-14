CREATE OR REPLACE FUNCTION dh.reset_day() RETURNS void AS $$
BEGIN
	UPDATE dh.users SET dh.users.day_all=0, dh.users.day_slack=0
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dh.reset_month() RETURNS void AS $$
BEGIN
	UPDATE dh.users SET dh.users.month_all=0, dh.users.month_slack=0
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION dh.add_user(nickname char[20], device_id uuid, university_id integer, name char[100], country char[100]) RETURNS void AS $$
BEGIN
	INSETR INTO dh.users ( nickname, device_id, university_id,
	VALUES (nickname, device_id, university_id)

	INSERT INTO dh.universities (university_id, name, country)
	VALUES (university_id, name, country)
END
$$
LANGUAGE plpgsql;