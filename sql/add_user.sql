CREATE OR REPLACE FUNCTION dh.add_user(nickname char[20], device_id uuid, university_id integer, name char[100], country char[100]) RETURNS void AS $$

	INSETR INTO dh.users ( nickname, device_id, university_id,
	VALUES (nickname, device_id, university_id)

	INSERT INTO dh.universities (university_id, name, country)
	VALUES (university_id, name, country)
$$
LANGUAGE sql