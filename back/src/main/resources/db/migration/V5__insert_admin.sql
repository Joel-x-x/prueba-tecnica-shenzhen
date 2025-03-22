-- Insertar el rol ADMIN
INSERT INTO roles (id, name)
VALUES ('FA9EFACE-157A-4F55-9BAB-72045096E5F2', 'ADMIN');

-- Insertar el usuario admin
INSERT INTO users (id, email, first_names, last_names, password, token)
VALUES ('703238D3-DCBB-40A0-9839-A958F92D88DF', 'admin@gmail.com', 'Admin', 'Monadmin',
        '$2a$10$DLgKrqzz6gwnyGnb8/JoV.h/YNGa8Z3FY6vNcMMGYhOQopwuTvzRq', '93585'); -- Password admin@gmail.com

-- Relación entre el usuario admin y el rol ADMIN en la tabla users_roles
INSERT INTO user_roles (user_id, role_id)
VALUES ('703238D3-DCBB-40A0-9839-A958F92D88DF', 'FA9EFACE-157A-4F55-9BAB-72045096E5F2');