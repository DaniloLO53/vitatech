-- ==========================================
-- FLYWAY SCRIPT: V2__Insert_Initial_Data.sql
-- Objetivo: Popular o catálogo de atividades, locais e alimentos.
-- ==========================================

-- 1. INSERIR LOCAIS DE ATIVIDADE (Com foco na geografia do RJ)
INSERT INTO activity_locations (name, effort_multiplier) VALUES
('Academia / Ginásio', 1.00),
('Casa', 1.00),
('Asfalto / Calçadão (Ex: Copacabana, Engenhão)', 1.00),
('Pista de Atletismo (Tartan)', 0.95),
('Grama / Campo', 1.05),
('Areia Fofa (Ex: Praia de Ipanema)', 1.30),
('Areia Batida', 1.15),
('Trilha Leve', 1.20),
('Trilha com Subida (Ex: Pedra da Gávea, Floresta da Tijuca)', 1.40),
('Piscina', 1.00);

-- 2. INSERIR ATIVIDADES FÍSICAS
-- Gasto aproximado de calorias por minuto para um adulto médio (70kg)
INSERT INTO physical_activities (name, base_calories_per_minute) VALUES
('Musculação (Intenso)', 6.0),
('Musculação (Moderado)', 4.5),
('Treino Funcional / HIIT', 10.0),
('Caminhada (Leve)', 4.0),
('Caminhada Rápida', 6.0),
('Corrida (Leve - Trote)', 8.5),
('Corrida (Moderada)', 11.5),
('Ciclismo (Moderado)', 8.0),
('Natação (Livre)', 9.5),
('Yoga / Alongamento', 3.5),
('Pilates', 4.5),
('Jiu-Jitsu / Lutas', 10.5),
('Crossfit', 12.0),
('Futebol', 9.0),
('Futvôlei', 9.5),
('Altinha', 7.5),
('Vôlei de Praia', 8.0);

-- 3. INSERIR ALIMENTOS BASE (Valores por 100g)
-- Atenção: created_by_user_id fica NULL porque são alimentos do sistema
INSERT INTO foods (name, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g) VALUES
-- Proteínas Animais
('Peito de Frango (Grelhado)', 165.0, 31.0, 0.0, 3.6),
('Carne Bovina - Alcatra (Grelhada)', 243.0, 26.0, 0.0, 15.0),
('Ovo de Galinha (Cozido)', 155.0, 13.0, 1.1, 11.0),
('Patinho Moído (Cozido)', 219.0, 27.0, 0.0, 11.0),
('Filé de Tilápia (Assado)', 128.0, 26.0, 0.0, 2.7),

-- Carboidratos Base
('Arroz Branco (Cozido)', 130.0, 2.7, 28.0, 0.3),
('Arroz Integral (Cozido)', 112.0, 2.6, 23.0, 0.9),
('Batata Inglesa (Cozida)', 86.0, 1.7, 20.0, 0.1),
('Batata Doce (Cozida)', 86.0, 1.6, 20.0, 0.1),
('Macarrão de Trigo (Cozido)', 138.0, 4.5, 29.0, 0.5),
('Pão Francês', 300.0, 8.0, 58.0, 3.0),
('Tapioca (Goma Manteiga)', 336.0, 0.0, 82.0, 0.0),

-- Leguminosas (Clássicos Brasileiros)
('Feijão Preto (Cozido)', 132.0, 8.9, 23.7, 0.5),
('Feijão Carioca (Cozido)', 76.0, 4.8, 13.6, 0.5),
('Grão de Bico (Cozido)', 164.0, 8.9, 27.4, 2.6),

-- Frutas
('Banana Prata', 89.0, 1.1, 22.8, 0.3),
('Maçã Fuji (com casca)', 52.0, 0.3, 13.8, 0.2),
('Polpa de Açaí (Sem Xarope)', 58.0, 0.8, 6.2, 3.9),
('Mamão Papaia', 43.0, 0.5, 11.0, 0.1),

-- Laticínios e Outros
('Queijo Minas Frescal', 243.0, 15.0, 3.0, 19.0),
('Leite Integral', 61.0, 3.2, 4.8, 3.2),
('Whey Protein Concentrado (Pó)', 379.0, 76.0, 10.0, 3.0),
('Azeite de Oliva Extravirgem', 884.0, 0.0, 0.0, 100.0),

-- Itens Cariocas / Regionais Extras
('Biscoito Globo (Salgado)', 424.0, 1.4, 80.2, 10.8),
('Mate Leão (Copo Tradicional)', 33.0, 0.0, 8.0, 0.0);