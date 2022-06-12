import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Employee from './employee';
import EmployeeDetail from './employee-detail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmployeeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Employee} />
    </Switch>
  </>
);

export default Routes;
