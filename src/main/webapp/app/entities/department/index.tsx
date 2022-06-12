import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Department from './department';
import DepartmentDetail from './department-detail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DepartmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Department} />
    </Switch>
  </>
);

export default Routes;
