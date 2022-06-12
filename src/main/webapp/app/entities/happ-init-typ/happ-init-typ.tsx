import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappInitTyp } from 'app/shared/model/happ-init-typ.model';
import { getEntities } from './happ-init-typ.reducer';

export const HappInitTyp = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const happInitTypList = useAppSelector(state => state.happInitTyp.entities);
  const loading = useAppSelector(state => state.happInitTyp.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="happ-init-typ-heading" data-cy="HappInitTypHeading">
        <Translate contentKey="happinessInitiativesApp.happInitTyp.home.title">Happ Init Typs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="happinessInitiativesApp.happInitTyp.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/happ-init-typ/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="happinessInitiativesApp.happInitTyp.home.createLabel">Create new Happ Init Typ</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {happInitTypList && happInitTypList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happInitTyp.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="happinessInitiativesApp.happInitTyp.initName">Init Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {happInitTypList.map((happInitTyp, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/happ-init-typ/${happInitTyp.id}`} color="link" size="sm">
                      {happInitTyp.id}
                    </Button>
                  </td>
                  <td>{happInitTyp.initName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/happ-init-typ/${happInitTyp.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/happ-init-typ/${happInitTyp.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/happ-init-typ/${happInitTyp.id}/delete`}
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
              <Translate contentKey="happinessInitiativesApp.happInitTyp.home.notFound">No Happ Init Typs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default HappInitTyp;
