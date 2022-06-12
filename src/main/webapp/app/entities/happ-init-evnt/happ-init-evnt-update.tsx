import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHappEvntTyp } from 'app/shared/model/happ-evnt-typ.model';
import { getEntities as getHappEvntTyps } from 'app/entities/happ-evnt-typ/happ-evnt-typ.reducer';
import { IHappInitTyp } from 'app/shared/model/happ-init-typ.model';
import { getEntities as getHappInitTyps } from 'app/entities/happ-init-typ/happ-init-typ.reducer';
import { IHappTargt } from 'app/shared/model/happ-targt.model';
import { getEntities as getHappTargts } from 'app/entities/happ-targt/happ-targt.reducer';
import { IHappEvntPlac } from 'app/shared/model/happ-evnt-plac.model';
import { getEntities as getHappEvntPlacs } from 'app/entities/happ-evnt-plac/happ-evnt-plac.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IBeneficiary } from 'app/shared/model/beneficiary.model';
import { getEntities as getBeneficiaries } from 'app/entities/beneficiary/beneficiary.reducer';
import { IHappInitEvnt } from 'app/shared/model/happ-init-evnt.model';
import { InitOrEvent } from 'app/shared/model/enumerations/init-or-event.model';
import { getEntity, updateEntity, createEntity, reset } from './happ-init-evnt.reducer';

export const HappInitEvntUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const happEvntTyps = useAppSelector(state => state.happEvntTyp.entities);
  const happInitTyps = useAppSelector(state => state.happInitTyp.entities);
  const happTargts = useAppSelector(state => state.happTargt.entities);
  const happEvntPlacs = useAppSelector(state => state.happEvntPlac.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const beneficiaries = useAppSelector(state => state.beneficiary.entities);
  const happInitEvntEntity = useAppSelector(state => state.happInitEvnt.entity);
  const loading = useAppSelector(state => state.happInitEvnt.loading);
  const updating = useAppSelector(state => state.happInitEvnt.updating);
  const updateSuccess = useAppSelector(state => state.happInitEvnt.updateSuccess);
  const initOrEventValues = Object.keys(InitOrEvent);
  const handleClose = () => {
    props.history.push('/happ-init-evnt' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getHappEvntTyps({}));
    dispatch(getHappInitTyps({}));
    dispatch(getHappTargts({}));
    dispatch(getHappEvntPlacs({}));
    dispatch(getEmployees({}));
    dispatch(getBeneficiaries({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...happInitEvntEntity,
      ...values,
      employees: mapIdList(values.employees),
      beneficiaries: mapIdList(values.beneficiaries),
      happEvntTyp: happEvntTyps.find(it => it.id.toString() === values.happEvntTyp.toString()),
      happInitTyp: happInitTyps.find(it => it.id.toString() === values.happInitTyp.toString()),
      happTargt: happTargts.find(it => it.id.toString() === values.happTargt.toString()),
      happEvntPlac: happEvntPlacs.find(it => it.id.toString() === values.happEvntPlac.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          initOrEvent: 'INITIATIVE',
          ...happInitEvntEntity,
          happEvntTyp: happInitEvntEntity?.happEvntTyp?.id,
          happInitTyp: happInitEvntEntity?.happInitTyp?.id,
          happTargt: happInitEvntEntity?.happTargt?.id,
          happEvntPlac: happInitEvntEntity?.happEvntPlac?.id,
          employees: happInitEvntEntity?.employees?.map(e => e.id.toString()),
          beneficiaries: happInitEvntEntity?.beneficiaries?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="happinessInitiativesApp.happInitEvnt.home.createOrEditLabel" data-cy="HappInitEvntCreateUpdateHeading">
            <Translate contentKey="happinessInitiativesApp.happInitEvnt.home.createOrEditLabel">Create or edit a HappInitEvnt</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="happ-init-evnt-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.initEventName')}
                id="happ-init-evnt-initEventName"
                name="initEventName"
                data-cy="initEventName"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.dateFrom')}
                id="happ-init-evnt-dateFrom"
                name="dateFrom"
                data-cy="dateFrom"
                type="date"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.dateTo')}
                id="happ-init-evnt-dateTo"
                name="dateTo"
                data-cy="dateTo"
                type="date"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.totalTarget')}
                id="happ-init-evnt-totalTarget"
                name="totalTarget"
                data-cy="totalTarget"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.notes')}
                id="happ-init-evnt-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.initOrEvent')}
                id="happ-init-evnt-initOrEvent"
                name="initOrEvent"
                data-cy="initOrEvent"
                type="select"
              >
                {initOrEventValues.map(initOrEvent => (
                  <option value={initOrEvent} key={initOrEvent}>
                    {translate('happinessInitiativesApp.InitOrEvent.' + initOrEvent)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.addDate')}
                id="happ-init-evnt-addDate"
                name="addDate"
                data-cy="addDate"
                type="date"
              />
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.addBy')}
                id="happ-init-evnt-addBy"
                name="addBy"
                data-cy="addBy"
                type="text"
              />
              <ValidatedField
                id="happ-init-evnt-happEvntTyp"
                name="happEvntTyp"
                data-cy="happEvntTyp"
                label={translate('happinessInitiativesApp.happInitEvnt.happEvntTyp')}
                type="select"
              >
                <option value="" key="0" />
                {happEvntTyps
                  ? happEvntTyps.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="happ-init-evnt-happInitTyp"
                name="happInitTyp"
                data-cy="happInitTyp"
                label={translate('happinessInitiativesApp.happInitEvnt.happInitTyp')}
                type="select"
              >
                <option value="" key="0" />
                {happInitTyps
                  ? happInitTyps.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="happ-init-evnt-happTargt"
                name="happTargt"
                data-cy="happTargt"
                label={translate('happinessInitiativesApp.happInitEvnt.happTargt')}
                type="select"
              >
                <option value="" key="0" />
                {happTargts
                  ? happTargts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="happ-init-evnt-happEvntPlac"
                name="happEvntPlac"
                data-cy="happEvntPlac"
                label={translate('happinessInitiativesApp.happInitEvnt.happEvntPlac')}
                type="select"
              >
                <option value="" key="0" />
                {happEvntPlacs
                  ? happEvntPlacs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.employee')}
                id="happ-init-evnt-employee"
                data-cy="employee"
                type="select"
                multiple
                name="employees"
              >
                <option value="" key="0" />
                {employees
                  ? employees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.slmName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('happinessInitiativesApp.happInitEvnt.beneficiary')}
                id="happ-init-evnt-beneficiary"
                data-cy="beneficiary"
                type="select"
                multiple
                name="beneficiaries"
              >
                <option value="" key="0" />
                {beneficiaries
                  ? beneficiaries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fullName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/happ-init-evnt" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HappInitEvntUpdate;
