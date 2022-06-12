import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappInitEvnt from './happ-init-evnt';
import HappInitEvntDetail from './happ-init-evnt-detail';
import HappInitEvntUpdate from './happ-init-evnt-update';
import HappInitEvntDeleteDialog from './happ-init-evnt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HappInitEvntUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HappInitEvntUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HappInitEvntDetail} />
      <ErrorBoundaryRoute path={match.url} component={HappInitEvnt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HappInitEvntDeleteDialog} />
  </>
);

export default Routes;
