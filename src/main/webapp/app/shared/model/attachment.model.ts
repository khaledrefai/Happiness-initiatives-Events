import { IHappInitEvnt } from 'app/shared/model/happ-init-evnt.model';

export interface IAttachment {
  id?: number;
  attachFileContentType?: string;
  attachFile?: string;
  happInitEvnt?: IHappInitEvnt | null;
}

export const defaultValue: Readonly<IAttachment> = {};
