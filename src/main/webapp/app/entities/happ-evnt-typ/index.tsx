import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappEvntTyp from './happ-evnt-typ';
import HappEvntTypDetail from './happ-evnt-typ-detail';
import HappEvntTypUpdate from './happ-evnt-typ-update';
import HappEvntTypDeleteDialog from './happ-evnt-typ-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HappEvntTypUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HappEvntTypUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HappEvntTypDetail} />
      <ErrorBoundaryRoute path={match.url} component={HappEvntTyp} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HappEvntTypDeleteDialog} />
  </>
);

export default Routes;
