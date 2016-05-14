CREATE OR REPLACE FUNCTION dh.reset_day() RETURNS void AS $$
	UPDATE dh.users U SET U.day_all=0, U.day_slack=0
$$ LANGUAGE sql;