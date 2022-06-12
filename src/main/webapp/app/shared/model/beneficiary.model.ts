import dayjs from 'dayjs';
import { IHappInitEvnt } from 'app/shared/model/happ-init-evnt.model';

export interface IBeneficiary {
  id?: number;
  eid?: string | null;
  uid?: string | null;
  fullName?: string | null;
  fullNameEn?: string | null;
  grpNumber?: number | null;
  nationality?: number | null;
  genderId?: number | null;
  birthDate?: string | null;
  happInitEvnts?: IHappInitEvnt[] | null;
}

export const defaultValue: Readonly<IBeneficiary> = {};
