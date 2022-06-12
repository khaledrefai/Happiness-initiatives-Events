import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HappTargt from './happ-targt';
import HappTargtDetail from './happ-targt-detail';
import HappTargtUpdate from './happ-targt-update';
import HappTargtDeleteDialog from './happ-targt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HappTargtUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HappTargtUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HappTargtDetail} />
      <ErrorBoundaryRoute path={match.url} component={HappTargt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HappTargtDeleteDialog} />
  </>
);

export default Routes;
