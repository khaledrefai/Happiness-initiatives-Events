import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappInitTyp from './happ-init-typ';
import HappInitTypDetail from './happ-init-typ-detail';
import HappInitTypUpdate from './happ-init-typ-update';
import HappInitTypDeleteDialog from './happ-init-typ-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HappInitTypUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HappInitTypUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HappInitTypDetail} />
      <ErrorBoundaryRoute path={match.url} component={HappInitTyp} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HappInitTypDeleteDialog} />
  </>
);

export default Routes;
