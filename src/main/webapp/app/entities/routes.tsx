import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappEvntTyp from './happ-evnt-typ';
import HappInitTyp from './happ-init-typ';
import HappTargt from './happ-targt';
import Department from './department';
import Employee from './employee';
import HappEvntPlac from './happ-evnt-plac';
import Beneficiary from './beneficiary';
import HappInitEvnt from './happ-init-evnt';
import Attachment from './attachment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}happ-evnt-typ`} component={HappEvntTyp} />
        <ErrorBoundaryRoute path={`${match.url}happ-init-typ`} component={HappInitTyp} />
        <ErrorBoundaryRoute path={`${match.url}happ-targt`} component={HappTargt} />
        <ErrorBoundaryRoute path={`${match.url}department`} component={Department} />
        <ErrorBoundaryRoute path={`${match.url}employee`} component={Employee} />
        <ErrorBoundaryRoute path={`${match.url}happ-evnt-plac`} component={HappEvntPlac} />
        <ErrorBoundaryRoute path={`${match.url}beneficiary`} component={Beneficiary} />
        <ErrorBoundaryRoute path={`${match.url}happ-init-evnt`} component={HappInitEvnt} />
        <ErrorBoundaryRoute path={`${match.url}attachment`} component={Attachment} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
