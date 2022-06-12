import dayjs from 'dayjs';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IHappEvntTyp } from 'app/shared/model/happ-evnt-typ.model';
import { IHappInitTyp } from 'app/shared/model/happ-init-typ.model';
import { IHappTargt } from 'app/shared/model/happ-targt.model';
import { IHappEvntPlac } from 'app/shared/model/happ-evnt-plac.model';
import { IEmployee } from 'app/shared/model/employee.model';
import { IBeneficiary } from 'app/shared/model/beneficiary.model';
import { InitOrEvent } from 'app/shared/model/enumerations/init-or-event.model';

export interface IHappInitEvnt {
  id?: number;
  initEventName?: string | null;
  dateFrom?: string | null;
  dateTo?: string | null;
  totalTarget?: number | null;
  notes?: string | null;
  initOrEvent?: InitOrEvent | null;
  addDate?: string | null;
  addBy?: number | null;
  attachments?: IAttachment[] | null;
  happEvntTyp?: IHappEvntTyp | null;
  happInitTyp?: IHappInitTyp | null;
  happTargt?: IHappTargt | null;
  happEvntPlac?: IHappEvntPlac | null;
  employees?: IEmployee[] | null;
  beneficiaries?: IBeneficiary[] | null;
}

export const defaultValue: Readonly<IHappInitEvnt> = {};
