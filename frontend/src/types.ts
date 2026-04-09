// ---- Workout ----
export interface WorkoutResponse {
  id: number;
  workoutType: string;
  durationMinutes: number;
  caloriesBurned: number | null;
  notes: string | null;
  date: string;
}

export interface WorkoutForm {
  workoutType: string;
  durationMinutes: number;
  caloriesBurned: string;
  notes: string;
  date: string;
}

// ---- Sleep ----
export interface SleepLogResponse {
  id: number;
  bedtime: string;
  wakeTime: string;
  durationHours: number;
  sleepQuality: number;
  notes: string | null;
  date: string;
}

export interface SleepForm {
  bedtime: string;
  wakeTime: string;
  durationHours: number;
  sleepQuality: number;
  notes: string;
  date: string;
}

// ---- Nutrition ----
export interface NutritionLogResponse {
  id: number;
  mealType: string;
  description: string;
  calories: number | null;
  proteinGrams: number | null;
  carbsGrams: number | null;
  fatGrams: number | null;
  date: string;
}

export interface NutritionForm {
  mealType: string;
  description: string;
  calories: string;
  proteinGrams: string;
  carbsGrams: string;
  fatGrams: string;
  date: string;
}

// ---- Wellbeing ----
export interface WellbeingLogResponse {
  id: number;
  moodRating: number;
  stressLevel: number;
  energyLevel: number;
  notes: string | null;
  date: string;
}

export interface WellbeingForm {
  moodRating: number;
  stressLevel: number;
  energyLevel: number;
  notes: string;
  date: string;
}
