INSERT INTO employee(id,
                     degree,
                     first_name,
                     last_name,
                     name,
                     national_id,
                     gender,
                     gross_salary,
                     years_of_experience)
VALUES (1, 'SENIOR', 'hamada', 'elgin', 'hamada elgin','102020','MALE', 10000, 35);


INSERT INTO user_credentials(user_name, password, employee_id,user_role)
VALUES ('hamada elgin', '$2a$12$AMsg.IGBdS3blFFG7K58wu3D2MuI/fPtvcKMD/FWLFHgA75b3mOcy', 1,'HR'); -- pw 123