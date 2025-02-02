insert into swift_codes(id, swift_code, country_iso2_code, is_headquarter, name, code_type, address , town_name, country_name, time_zone , parent_id)
values(1, 'AAISALTRXXX', 'AL', true, 'UNITED BANK OF ALBANIA SH.A', 'BIC11', 'HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023',
	'TIRANA', 'ALBANIA', 'Europe/Tirane', null)
ON CONFLICT (id) DO NOTHING;