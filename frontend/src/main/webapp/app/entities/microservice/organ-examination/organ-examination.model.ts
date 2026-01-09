export interface IOrganExamination {
  id: number;
  cardiovascular?: string | null;
  respiratory?: string | null;
  digestive?: string | null;
  urinary?: string | null;
  musculoskeletal?: string | null;
  endocrine?: string | null;
  neurological?: string | null;
  psychiatric?: string | null;
  surgery?: string | null;
  obstetricsAndGynecology?: string | null;
  otolaryngology?: string | null;
  dentistryAndMaxillofacialSurgery?: string | null;
  eye?: string | null;
  dermatology?: string | null;
  nutrition?: string | null;
  exercise?: string | null;
  other?: string | null;
  developmentAssessment?: string | null;
}

export type NewOrganExamination = Omit<IOrganExamination, 'id'> & { id: null };
