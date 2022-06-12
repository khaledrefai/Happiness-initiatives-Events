import happEvntTyp from 'app/entities/happ-evnt-typ/happ-evnt-typ.reducer';
import happInitTyp from 'app/entities/happ-init-typ/happ-init-typ.reducer';
import happTargt from 'app/entities/happ-targt/happ-targt.reducer';
import department from 'app/entities/department/department.reducer';
import employee from 'app/entities/employee/employee.reducer';
import happEvntPlac from 'app/entities/happ-evnt-plac/happ-evnt-plac.reducer';
import beneficiary from 'app/entities/beneficiary/beneficiary.reducer';
import happInitEvnt from 'app/entities/happ-init-evnt/happ-init-evnt.reducer';
import attachment from 'app/entities/attachment/attachment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  happEvntTyp,
  happInitTyp,
  happTargt,
  department,
  employee,
  happEvntPlac,
  beneficiary,
  happInitEvnt,
  attachment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
