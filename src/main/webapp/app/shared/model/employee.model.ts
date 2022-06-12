import { IDepartment } from 'app/shared/model/department.model';
import { IHappInitEvnt } from 'app/shared/model/happ-init-evnt.model';

export interface IEmployee {
  id?: number;
  slmName?: string | null;
  department?: IDepartment | null;
  happInitEvnts?: IHappInitEvnt[] | null;
}

export const defaultValue: Readonly<IEmployee> = {};
