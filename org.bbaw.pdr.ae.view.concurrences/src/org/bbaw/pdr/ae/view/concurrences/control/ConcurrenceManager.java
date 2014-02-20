/**
 * This file is part of Archiv-Editor.
 * 
 * The software Archiv-Editor serves as a client user interface for working with
 * the Person Data Repository. See: pdr.bbaw.de
 * 
 * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
 * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
 * www.bbaw.de
 * 
 * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
 * of Sciences and Humanities
 * 
 * The software Archiv-Editor was developed by @author: Christoph Plutte.
 * 
 * Archiv-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Archiv-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Archiv-Editor.  
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package org.bbaw.pdr.ae.view.concurrences.control;

import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Concurrences;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.view.concurrences.internal.Activator;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class ConcurrenceManager.
 * @author Christoph Plutte
 */
public class ConcurrenceManager
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/**
	 * Merge aspect.
	 * @param objectAspect the object aspect
	 * @param targetId the target id
	 * @param objectId the object id
	 * @return the aspect
	 */
	private Aspect mergeAspect(final Aspect objectAspect, final PdrId targetId, final PdrId objectId)
	{
		if (objectAspect.getRelationDim() != null && objectAspect.getRelationDim().getRelationStms() != null)
		{
			for (RelationStm rStm : objectAspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject().equals(objectId))
				{
					rStm.setSubject(targetId);
				}
				else
				{
					if (rStm.getRelations() != null)
					{
						for (Relation r : rStm.getRelations())
						{
							if (r.getObject().equals(objectId))
							{
								r.setObject(targetId);
							}

						}
					}
				}
			}
		}
		if (objectAspect.getRangeList() != null)
		{
			for (TaggingRange tr : objectAspect.getRangeList())
			{
				if (tr.getAna() != null && tr.getAna().equals(objectId.toString()))
				{
					tr.setAna(targetId.toString());
				}
			}
		}
		Status log;
		String provider = Activator.getDefault().getPreferenceStore()
				.getString("PRIMARY_SEMANTIC_PROVIDER").toUpperCase(); //$NON-NLS-1$
		if (objectAspect.getSemanticDim() != null && objectAspect.getSemanticDim().getSemanticStms() != null)
		{
			for (SemanticStm sStm : objectAspect.getSemanticDim().getSemanticStms())
			{
				if (_facade.getPersonDisplayNameTags(null).contains(sStm.getLabel())
						|| sStm.getLabel().startsWith("NormName"))
				{
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
							"ConcurrenceManager aspect with displayname merged: " + objectAspect.getPdrId().toString());
					iLogger.log(log);
					Vector<String> sems = _facade.getPersonNameTags(provider);
					if (sems == null)
					{
						sems = _facade.getPersonNameTags(null);
					}
					if (sems == null || sems.isEmpty() || sems.firstElement() == null)
					{
						sStm.setLabel("Name");
					}
					else
					{
						sStm.setLabel(sems.firstElement());
					}

				}
			}
		}
		Revision revision = new Revision();
		revision.setRevisor(_facade.getCurrentUser().getDisplayName());
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId());
		revision.setRef(objectAspect.getRecord().getRevisions().size());
		objectAspect.getRecord().getRevisions().add(revision);
		objectAspect.setDirty(true);

		return objectAspect;

	}

	/**
	 * Merge person.
	 * @param targetPerson the target person
	 * @param objectPerson the object person
	 * @return the person
	 * @throws Exception the exception
	 */
	public final Person mergePerson(final Person targetPerson, final Person objectPerson) throws Exception
	{
		IDBManager dbm = _facade.getDBManager();
		PDRObjectsProvider oProvider = new PDRObjectsProvider();
		oProvider.setInput(objectPerson);
		Vector<Aspect> objectAspects = oProvider.getAspects();
		oProvider.setInput(targetPerson);
		Vector<Aspect> targetAspects = oProvider.getAspects();
		Aspect changedAspect;
		boolean contains = false;
		for (Aspect objectAspect : objectAspects)
		{
			for (Aspect targetAspect : targetAspects)
			{
				if (targetAspect.equalsContent(objectAspect)
						&& targetAspect
								.similarRelations(objectAspect, targetPerson.getPdrId(), objectPerson.getPdrId()))
				{
					contains = true;
				}
			}
			if (!contains)
			{
				changedAspect = mergeAspect(objectAspect, targetPerson.getPdrId(), objectPerson.getPdrId());
				if (!targetPerson.getAspectIds().contains(changedAspect.getPdrId().toString()))
				{
					targetPerson.getAspectIds().add(changedAspect.getPdrId());
					// _facade.saveAspect(changedAspect);
				}

				dbm.saveToDB(changedAspect);

			}
			else
			{
				dbm.delete(objectAspect.getPdrId(), "aspect"); //$NON-NLS-1$ //$NON-NLS-2$
				_facade.deleteAspect(objectAspect);
			}
		}
		if (objectPerson.getIdentifiers() != null && !objectPerson.getIdentifiers().getIdentifiers().isEmpty())
		{
			// TOTO identifier und concurrences vergleichen und verschmelzen
			if (targetPerson.getIdentifiers() != null && !targetPerson.getIdentifiers().getIdentifiers().isEmpty())
			{
				for (Identifier objectIdentifier : objectPerson.getIdentifiers().getIdentifiers())
				{
					for (Identifier targetIdentifier : targetPerson.getIdentifiers().getIdentifiers())
					{
						if (targetIdentifier.equals(objectIdentifier))
						{
							contains = true;
						}
					}
					if (!contains)
					{
						targetPerson.getIdentifiers().getIdentifiers().add(objectIdentifier);
					}
				}
			}
			targetPerson.setIdentifiers(objectPerson.getIdentifiers());
		}
		if (objectPerson.getConcurrences() != null && !objectPerson.getConcurrences().getConcurrences().isEmpty())
		{
			// TOTO identifier und concurrences vergleichen und verschmelzen
			if (targetPerson.getConcurrences() != null && !targetPerson.getConcurrences().getConcurrences().isEmpty())
			{
				for (Concurrence objectConcurrence : objectPerson.getConcurrences().getConcurrences())
				{
					for (Concurrence targetConcurrence : targetPerson.getConcurrences().getConcurrences())
					{
						if (targetConcurrence.equals(objectConcurrence))
						{
							contains = true;
						}
					}
					if (!contains)
					{
						targetPerson.getConcurrences().getConcurrences().add(objectConcurrence);
					}
				}
			}
			targetPerson.setConcurrences(objectPerson.getConcurrences());
		}
		return targetPerson;

	}

	/**
	 * Sets the concurrence.
	 * @param person the person
	 * @param id the id
	 * @return the person
	 */
	public final Person setConcurrence(final Person person, final PdrId id)
	{
		Concurrence c = new Concurrence();
		c.setPersonId(id);
		if (person.getConcurrences() == null)
		{
			person.setConcurrences(new Concurrences());
			person.getConcurrences().setConcurrences(new Vector<Concurrence>());
		}
		person.getConcurrences().getConcurrences().add(c);

		Revision revision = new Revision();
		revision.setRevisor(_facade.getCurrentUser().getDisplayName());
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId());
		revision.setRef(person.getRecord().getRevisions().size());
		person.getRecord().getRevisions().add(revision);
		person.setDirty(true);

		return person;

	}

}
