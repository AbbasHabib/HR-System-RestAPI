INSERT INTO attendance_table(id, initial_working_years)
VALUES (1, 9);

INSERT INTO employee(id,
                     degree,
                     first_name,
                     last_name,
                     national_id,
                     gender,
                     gross_salary,
                     net_salary,
                     role,
                     years_of_experience,
                     graduation_date,
                     attendance_table_id)
VALUES (1, 'SENIOR', 'hamada', 'elgin', '123', 'MALE', 10000, 8000, 'HR', 35, '2012-01-01', 1);

INSERT INTO user_credentials(user_name, password, employee_id, user_role)
VALUES ('hamada_elgin_1', '$2a$12$AMsg.IGBdS3blFFG7K58wu3D2MuI/fPtvcKMD/FWLFHgA75b3mOcy', 1, 'HR'); -- pw 123
