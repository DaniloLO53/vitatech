-- ==========================================
-- 0. CRIAÇÃO DE TIPOS ENUMERADOS
-- ==========================================
CREATE TYPE user_role AS ENUM ('PATIENT', 'NUTRITIONIST', 'ADMIN');
CREATE TYPE connection_status AS ENUM ('PENDING', 'ACTIVE', 'REJECTED', 'INACTIVE');
CREATE TYPE meal_type_enum AS ENUM ('BREAKFAST', 'LUNCH', 'SNACK', 'DINNER', 'SUPPER');
-- ==========================================
-- 1. USUÁRIOS E AUTENTICAÇÃO (LGPD & Perfis)
-- ==========================================
CREATE TABLE users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role user_role DEFAULT 'PATIENT' NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Comentários LGPD e Negócio
COMMENT ON TABLE users IS 'Armazena dados dos usuários. Dados sensíveis protegidos via LGPD.';
COMMENT ON COLUMN users.email IS 'Email usado para login. Deve ser único.';
COMMENT ON COLUMN users.password_hash IS 'Senha criptografada obrigatoriamente com algoritmo BCrypt (Requisito LGPD de segurança).';

-- ==========================================
-- 2. RELACIONAMENTO PACIENTE <-> NUTRICIONISTA
-- ==========================================
CREATE TABLE patient_nutritionist (
    patient_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    nutritionist_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status connection_status DEFAULT 'PENDING' NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (patient_id, nutritionist_id)
);

COMMENT ON TABLE patient_nutritionist IS 'Gerencia o vínculo e o acompanhamento entre nutricionistas e pacientes.';

-- ==========================================
-- 3. MÓDULO DE ALIMENTAÇÃO
-- ==========================================
CREATE TABLE foods (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_by_user_id INT REFERENCES users(id) ON DELETE SET NULL,
    name VARCHAR(150) NOT NULL,
    calories_per_100g DECIMAL(10, 2) NOT NULL CHECK (calories_per_100g >= 0),
    protein_per_100g DECIMAL(10, 2) NOT NULL CHECK (protein_per_100g >= 0),
    carbs_per_100g DECIMAL(10, 2) NOT NULL CHECK (carbs_per_100g >= 0),
    fat_per_100g DECIMAL(10, 2) NOT NULL CHECK (fat_per_100g >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

COMMENT ON TABLE foods IS 'Catálogo de alimentos base focado na culinária carioca/brasileira e itens personalizados.';
COMMENT ON COLUMN foods.created_by_user_id IS 'Nulo para alimentos do sistema. Preenchido se um usuário criar seu próprio alimento/receita.';

CREATE TABLE meals (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    meal_type meal_type_enum NOT NULL,
    consumed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

COMMENT ON TABLE meals IS 'Cabeçalho da refeição registrada pelo paciente no diário alimentar.';

CREATE TABLE meal_items (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    meal_id INT NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
    food_id INT NOT NULL REFERENCES foods(id) ON DELETE RESTRICT,
    quantity_grams DECIMAL(10, 2) NOT NULL CHECK (quantity_grams > 0)
);

COMMENT ON TABLE meal_items IS 'Itens individuais consumidos em uma refeição.';
COMMENT ON COLUMN meal_items.quantity_grams IS 'Quantidade ingerida em gramas para permitir o cálculo automático dos macronutrientes pelo back-end.';

-- ==========================================
-- 4. MÓDULO DE ATIVIDADES FÍSICAS
-- ==========================================
CREATE TABLE physical_activities (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    base_calories_per_minute DECIMAL(10, 2) NOT NULL CHECK (base_calories_per_minute > 0)
);

COMMENT ON TABLE physical_activities IS 'Catálogo de atividades (Ex: Corrida, Treino Funcional, Ciclismo).';

CREATE TABLE activity_locations (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    effort_multiplier DECIMAL(4, 2) NOT NULL DEFAULT 1.0 CHECK (effort_multiplier > 0)
);

COMMENT ON TABLE activity_locations IS 'Locais locais pré-cadastrados do RJ/Méier.';
COMMENT ON COLUMN activity_locations.effort_multiplier IS 'Multiplicador de gasto calórico (Ex: Areia = 1.2, Asfalto = 1.0).';

CREATE TABLE user_activities (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    activity_id INT NOT NULL REFERENCES physical_activities(id) ON DELETE RESTRICT,
    location_id INT NOT NULL REFERENCES activity_locations(id) ON DELETE RESTRICT,
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    performed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

COMMENT ON TABLE user_activities IS 'Registro de atividades físicas realizadas pelo paciente.';

-- ==========================================
-- 5. COMUNICAÇÃO (CHAT VIA WEBSOCKETS)
-- ==========================================
CREATE TABLE messages (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_read BOOLEAN DEFAULT FALSE NOT NULL
);

COMMENT ON TABLE messages IS 'Histórico de mensagens do chat em tempo real entre nutricionista e paciente.';

-- ==========================================
-- 6. ÍNDICES DE OTIMIZAÇÃO (PERFORMANCE)
-- ==========================================
-- Índices para buscas frequentes (Foreign Keys e Filtros comuns)
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_foods_created_by ON foods(created_by_user_id);
CREATE INDEX idx_foods_name ON foods(name); -- Otimiza a busca textual de alimentos com paginação
CREATE INDEX idx_meals_user_date ON meals(user_id, consumed_at);
CREATE INDEX idx_meal_items_meal ON meal_items(meal_id);
CREATE INDEX idx_user_activities_user_date ON user_activities(user_id, performed_at);
CREATE INDEX idx_messages_participants ON messages(sender_id, receiver_id);
CREATE INDEX idx_messages_sent_at ON messages(sent_at);