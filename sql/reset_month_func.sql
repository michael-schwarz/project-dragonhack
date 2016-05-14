CREATE OR REPLACE FUNCTION dh.reset_month() RETURNS void AS $$
	UPDATE dh.users U SET U.month_all=0, U.month_slack=0
$$ LANGUAGE sql;