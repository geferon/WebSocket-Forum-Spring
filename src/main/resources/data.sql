INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_USER'), ('ROLE_MODERATOR'), ('ROLE_ADMIN');
INSERT IGNORE INTO categorias (nombre, descripcion, icono, color)
	VALUES ('Discusión general', 'Para cualquier tipo de conversación', 'chat', '#FF9F38'),
	('Juegos', 'Para la discusión de juegos', 'videogame_asset', '#1BFFB1'),
	('Videos y media', 'Para todo tipo de media digital', 'movie', '#B53FB1'),
	('Tecnología', 'Para la discusión de tecnología', 'dns', '#3B5592');
	
INSERT IGNORE INTO usuarios (email, fecha_registro, password, username, avatar)
	VALUES ('admin@geferon.net', CURRENT_TIMESTAMP, '$2a$10$MrjEBbddW5fwVZZyZ9abU.f0kt88Vjz1hJ.Q0hSfBsKGJO8mvs3s2', 'admin', 'perfil/default.jpg'),
	('usuario@geferon.net', CURRENT_TIMESTAMP, '$2a$10$MrjEBbddW5fwVZZyZ9abU.f0kt88Vjz1hJ.Q0hSfBsKGJO8mvs3s2', 'usuario', 'perfil/default.jpg');
	
INSERT IGNORE INTO `usuariosroles` (`id_usuario`, `id_rol`) VALUES ('1', 'ROLE_USER'), ('1', 'ROLE_ADMIN'), ('2', 'ROLE_USER');