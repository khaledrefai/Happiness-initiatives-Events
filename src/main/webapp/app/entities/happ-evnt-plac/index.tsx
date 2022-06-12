import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappEvntPlac from './happ-evnt-plac';
import HappEvntPlacDetail from './happ-evnt-plac-detail';
import HappEvntPlacUpdate from './happ-evnt-plac-update';
import HappEvntPlacDeleteDialog from './happ-evnt-plac-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HappEvntPlacUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HappEvntPlacUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HappEvntPlacDetail} />
      <ErrorBoundaryRoute path={match.url} component={HappEvntPlac} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HappEvntPlacDeleteDialog} />
  </>
);

export default Routes;
