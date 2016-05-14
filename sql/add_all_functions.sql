CREATE OR REPLACE FUNCTION reset_day() RETURNS void AS $$
BEGIN
	UPDATE users SET day_all=0, day_slack=0;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION reset_month() RETURNS void AS $$
BEGIN
	UPDATE users SET month_all=0, month_slack=0;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_user(nickname char(20), device_id uuid, university_id integer) RETURNS void AS $$
BEGIN
	INSERT INTO users ( nickname, device_id, university_id)
	VALUES (nickname, device_id, university_id);
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_nickname(nickname1 char(20)) RETURNS boolean AS $$
BEGIN
	RETURN (SELECT EXISTS (SELECT 1 FROM users WHERE nickname=nickname1));
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_nickname(nick varchar(20)) 
RETURNS TABLE (nickname1 char(20), device_id1 uuid, university_id1 integer)
AS $$ 
BEGIN
RETURN QUERY (SELECT (nickname , device_id, university_id ) FROM users WHERE nickname=nick);
END $$
LANGUAGE plpgsql;