export type MealKey = "CAFE_MANHA" | "ALMOCO" | "JANTAR" | "LANCHE" | "CEIA" | "POS_TREINO" | "OUTRO";
export type MeasureKey = "GRAMA" | "MILILITRO" | "UNIDADE" | "FATIA" | "XICARA" | "COLHER_SOPA" | "PEDACO";

export const MEAL_TITLES: Record<MealKey, string> = {
  CAFE_MANHA: "Café da Manhã",
  ALMOCO: "Almoço",
  JANTAR: "Jantar",
  LANCHE: "Lanche",
  CEIA: "Ceia",
  POS_TREINO: "Pós-Treino",
  OUTRO: "Outro",
};

export const UNIDADE_MEDIDA_LABELS: Record<MeasureKey, string> = {
  GRAMA: "g",
  MILILITRO: "ml",
  UNIDADE: "unidade",
  FATIA: "fatia",
  XICARA: "xícara",
  COLHER_SOPA: "colher de sopa",
  PEDACO: "pedaço",
};