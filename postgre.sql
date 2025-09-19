CREATE TYPE "tipo_refeicao" AS ENUM (
  'cafe_manha',
  'lanche',
  'almoco',
  'jantar',
  'ceia',
  'pos_treino',
  'outro'
);

CREATE TYPE "unidade_medida" AS ENUM (
  'g',
  'ml',
  'un',
  'porcao'
);

CREATE TYPE "nivel_atividade" AS ENUM (
  'sedentario',
  'leve',
  'moderado',
  'alto',
  'atleta'
);

CREATE TYPE "objetivo_usuario" AS ENUM (
  'perder_peso',
  'manter_peso',
  'ganhar_peso',
  'performance',
  'saude'
);

CREATE TABLE "usuarios" (
  "id_usuario" uuid PRIMARY KEY,
  "nome" varchar(120) NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "senha_hash" text NOT NULL,
  "altura_m" numeric(4,2),
  "data_nascimento" date NOT NULL,
  "nivel_atividade" nivel_atividade NOT NULL DEFAULT 'sedentario',
  "objetivo" objetivo_usuario NOT NULL DEFAULT 'saude',
  "criado_em" timestamptz NOT NULL DEFAULT (now()),
  "atualizado_em" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "alimentos" (
  "id_alimento" uuid PRIMARY KEY,
  "nome" varchar(160) UNIQUE NOT NULL,
  "calorias" numeric(10,3) NOT NULL,
  "proteinas_g" numeric(10,3) NOT NULL,
  "carboidratos_g" numeric(10,3) NOT NULL,
  "gorduras_g" numeric(10,3) NOT NULL,
  "criado_em" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "refeicoes" (
  "id_refeicao" uuid PRIMARY KEY,
  "id_usuario" uuid NOT NULL,
  "tipo" tipo_refeicao NOT NULL,
  "data_hora" timestamptz NOT NULL,
  "observacoes" text,
  "criado_em" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "itens_refeicao" (
  "id_item" uuid PRIMARY KEY,
  "id_refeicao" uuid NOT NULL,
  "id_alimento" uuid NOT NULL,
  "quantidade" numeric(12,3) NOT NULL,
  "unidade" unidade_medida NOT NULL,
  "observacoes" text
);

CREATE TABLE "registros_peso" (
  "id_registro" uuid PRIMARY KEY,
  "id_usuario" uuid NOT NULL,
  "peso_kg" numeric(6,3) NOT NULL,
  "data_medicao" date NOT NULL,
  "observado_em" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "registros_agua" (
  "id_registro" uuid PRIMARY KEY,
  "id_usuario" uuid NOT NULL,
  "quantidade_ml" int NOT NULL,
  "data_medicao" date NOT NULL,
  "observado_em" timestamptz NOT NULL DEFAULT (now())
);

CREATE INDEX "idx_refeicoes_usuario_data" ON "refeicoes" ("id_usuario", "data_hora");

CREATE INDEX "idx_itens_refeicao_refeicao" ON "itens_refeicao" ("id_refeicao");

CREATE INDEX "idx_itens_refeicao_alimento" ON "itens_refeicao" ("id_alimento");

CREATE INDEX "idx_registro_peso_usuario_data" ON "registros_peso" ("id_usuario", "data_medicao");

CREATE INDEX "idx_registro_agua_usuario_data" ON "registros_agua" ("id_usuario", "data_medicao");

COMMENT ON TABLE "usuarios" IS 'Trigger BEFORE UPDATE para atualizar atualizado_em (não representado em DBML).';

COMMENT ON COLUMN "usuarios"."altura_m" IS 'CHECK (altura_m > 0)';

COMMENT ON COLUMN "alimentos"."calorias" IS '>= 0';

COMMENT ON COLUMN "alimentos"."proteinas_g" IS '>= 0';

COMMENT ON COLUMN "alimentos"."carboidratos_g" IS '>= 0';

COMMENT ON COLUMN "alimentos"."gorduras_g" IS '>= 0';

COMMENT ON COLUMN "itens_refeicao"."quantidade" IS '> 0';

COMMENT ON COLUMN "registros_peso"."peso_kg" IS '> 0';

COMMENT ON COLUMN "registros_agua"."quantidade_ml" IS '> 0';

ALTER TABLE "refeicoes" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuarios" ("id_usuario") ON DELETE CASCADE;

ALTER TABLE "itens_refeicao" ADD FOREIGN KEY ("id_refeicao") REFERENCES "refeicoes" ("id_refeicao") ON DELETE CASCADE;

ALTER TABLE "itens_refeicao" ADD FOREIGN KEY ("id_alimento") REFERENCES "alimentos" ("id_alimento");

ALTER TABLE "registros_peso" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuarios" ("id_usuario") ON DELETE CASCADE;

ALTER TABLE "registros_agua" ADD FOREIGN KEY ("id_usuario") REFERENCES "usuarios" ("id_usuario") ON DELETE CASCADE;
