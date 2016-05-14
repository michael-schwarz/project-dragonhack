CREATE OR REPLACE FUNCTION reset_day() RETURNS void AS $$
BEGIN
	UPDATE users SET users.day_all=0, users.day_slack=0;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION reset_month() RETURNS void AS $$
BEGIN
	UPDATE users SET users.month_all=0, users.month_slack=0;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_user(nickname char[20], device_id uuid, university_id integer, name char[100], country char[100]) RETURNS void AS $$
BEGIN
	INSETR INTO users ( nickname, device_id, university_id)
	VALUES (nickname, device_id, university_id);

	INSERT INTO universities (university_id, name, country)
	VALUES (university_id, name, country);
END
$$
LANGUAGE plpgsql;