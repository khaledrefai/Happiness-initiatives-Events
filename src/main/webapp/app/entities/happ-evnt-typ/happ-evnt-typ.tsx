import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappEvntTyp } from 'app/shared/model/happ-evnt-typ.model';
import { getEntities } from './happ-evnt-typ.reducer';

export const HappEvntTyp = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const happEvntTypList = useAppSelector(state => state.happEvntTyp.entities);
  const loading = useAppSelector(state => state.happEvntTyp.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="happ-evnt-typ-heading" data-cy="HappEvntTypHeading">
        <Translate contentKey="happinessInitiativesApp.happEvntTyp.home.title">Happ Evnt Typs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="happinessInitiativesApp.happEvntTyp.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/happ-evnt-typ/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="happinessInitiativesApp.happEvntTyp.home.createLabel">Create new Happ Evnt Typ</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {happEvntTypList && happEvntTypList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happEvntTyp.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happEvntTyp.eventName">Event Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {happEvntTypList.map((happEvntTyp, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/happ-evnt-typ/${happEvntTyp.id}`} color="link" size="sm">
                      {happEvntTyp.id}
                    </Button>
                  </td>
                  <td>{happEvntTyp.eventName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/happ-evnt-typ/${happEvntTyp.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/happ-evnt-typ/${happEvntTyp.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/happ-evnt-typ/${happEvntTyp.id}/delete`}
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
              <Translate contentKey="happinessInitiativesApp.happEvntTyp.home.notFound">No Happ Evnt Typs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default HappEvntTyp;
