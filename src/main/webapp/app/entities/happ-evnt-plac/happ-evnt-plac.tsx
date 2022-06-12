import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappEvntPlac } from 'app/shared/model/happ-evnt-plac.model';
import { getEntities } from './happ-evnt-plac.reducer';

export const HappEvntPlac = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const happEvntPlacList = useAppSelector(state => state.happEvntPlac.entities);
  const loading = useAppSelector(state => state.happEvntPlac.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="happ-evnt-plac-heading" data-cy="HappEvntPlacHeading">
        <Translate contentKey="happinessInitiativesApp.happEvntPlac.home.title">Happ Evnt Placs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="happinessInitiativesApp.happEvntPlac.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/happ-evnt-plac/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="happinessInitiativesApp.happEvntPlac.home.createLabel">Create new Happ Evnt Plac</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {happEvntPlacList && happEvntPlacList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happEvntPlac.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happEvntPlac.placeName">Place Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {happEvntPlacList.map((happEvntPlac, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/happ-evnt-plac/${happEvntPlac.id}`} color="link" size="sm">
                      {happEvntPlac.id}
                    </Button>
                  </td>
                  <td>{happEvntPlac.placeName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/happ-evnt-plac/${happEvntPlac.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/happ-evnt-plac/${happEvntPlac.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/happ-evnt-plac/${happEvntPlac.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="happinessInitiativesApp.happEvntPlac.home.notFound">No Happ Evnt Placs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default HappEvntPlac;
