import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappTargt } from 'app/shared/model/happ-targt.model';
import { getEntities } from './happ-targt.reducer';

export const HappTargt = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const happTargtList = useAppSelector(state => state.happTargt.entities);
  const loading = useAppSelector(state => state.happTargt.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="happ-targt-heading" data-cy="HappTargtHeading">
        <Translate contentKey="happinessInitiativesApp.happTargt.home.title">Happ Targts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="happinessInitiativesApp.happTargt.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/happ-targt/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="happinessInitiativesApp.happTargt.home.createLabel">Create new Happ Targt</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {happTargtList && happTargtList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happTargt.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happTargt.targetName">Target Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {happTargtList.map((happTargt, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/happ-targt/${happTargt.id}`} color="link" size="sm">
                      {happTargt.id}
                    </Button>
                  </td>
                  <td>{happTargt.targetName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/happ-targt/${happTargt.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/happ-targt/${happTargt.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/happ-targt/${happTargt.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="happinessInitiativesApp.happTargt.home.notFound">No Happ Targts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default HappTargt;
