INSERT INTO roles (id, name)
VALUES (CAST('FA9EFACE-157A-4F55-9BAB-72045096E5F2' AS UNIQUEIDENTIFIER), 'ADMIN');

INSERT INTO users (id, email, first_names, last_names, password, token)
VALUES (CAST('703238D3-DCBB-40A0-9839-A958F92D88DF' AS UNIQUEIDENTIFIER), 'admin@gmail.com', 'Admin', 'Monadmin',
        '$2a$10$DLgKrqzz6gwnyGnb8/JoV.h/YNGa8Z3FY6vNcMMGYhOQopwuTvzRq', '93585');

INSERT INTO user_roles (user_id, role_id)
VALUES (
           CAST('703238D3-DCBB-40A0-9839-A958F92D88DF' AS UNIQUEIDENTIFIER),
           CAST('FA9EFACE-157A-4F55-9BAB-72045096E5F2' AS UNIQUEIDENTIFIER)
       );
GO