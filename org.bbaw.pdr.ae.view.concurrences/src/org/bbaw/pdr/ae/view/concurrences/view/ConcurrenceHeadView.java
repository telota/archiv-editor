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
package org.bbaw.pdr.ae.view.concurrences.view;

import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.ComplexName;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.view.concurrences.control.ConcurrenceManager;
import org.bbaw.pdr.ae.view.concurrences.internal.Activator;
import org.bbaw.pdr.ae.view.control.comparator.PdrObjectViewComparator;
import org.bbaw.pdr.ae.view.control.provider.PersonVectorContentProvider;
import org.bbaw.pdr.ae.view.control.provider.TableLabelProvider;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

/**
 * The Class ConcurrenceHeadView.
 * @author Christoph Plutte
 */
public class ConcurrenceHeadView extends ViewPart implements Observer
{

	/** _facade singleton instance. */
	private Facade _facade = Facade.getInstanz();
	/** MainSearcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();
	/** Logger. */
	private ILog _iLogger = AEConstants.ILOGGER;

	/** The concurrence manager. */
	private ConcurrenceManager _concurrenceManager = new ConcurrenceManager();

	/** The main sash form. */
	private SashForm _mainSashForm;

	/** The left composite. */
	private Composite _leftComposite;

	/** The right composite. */
	private Composite _rightComposite;

	/** The left button composite. */
	private Composite _leftButtonComposite;

	/** The right button composite. */
	private Composite _rightButtonComposite;

	/** The left group. */
	private Group _leftGroup;

	/** The right group. */
	private Group _rightGroup;

	/** The merge button left. */
	private Button _mergeButtonLeft;

	/** The merge button right. */
	private Button _mergeButtonRight;

	/** The set concurrence. */
	private Button _setConcurrence;

	/** The search button. */
	private Button _searchButton;

	/** The concurring person text. */
	private Text _concurringPersonText;

	/** The compare name. */
	private boolean _compareName;

	/** The compare birth. */
	private boolean _compareBirth;

	/** The compare death. */
	private boolean _compareDeath;

	/** The compare desc. */
	private boolean _compareDesc;

	/** The compare fuzzy. */
	private boolean _compareFuzzy;

	/** The person query. */
	private PdrQuery _personQuery;

	/** The person table viewer. */
	private TableViewer _personTableViewer;

	/** The parent shell. */
	private Shell _parentShell;

	/**
	 * Instantiates a new concurrence head view.
	 */
	public ConcurrenceHeadView()
	{
	}

	@Override
	public final void createPartControl(final Composite parent)
	{
		parent.setLayout(new GridLayout());
		_facade.addObserver(this);
		_mainSashForm = new SashForm(parent, SWT.HORIZONTAL);
		_mainSashForm.setLayoutData(new GridData());
		((GridData) _mainSashForm.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainSashForm.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainSashForm.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainSashForm.getLayoutData()).grabExcessVerticalSpace = true;
		_leftComposite = new Composite(_mainSashForm, SWT.NONE);
		_leftComposite.setLayout(new GridLayout());
		((GridLayout) _leftComposite.getLayout()).numColumns = 1;
		((GridLayout) _leftComposite.getLayout()).makeColumnsEqualWidth = false;
		_leftComposite.setLayoutData(new GridData());
		((GridData) _leftComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _leftComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _leftComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _leftComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_leftGroup = new Group(_leftComposite, SWT.NONE);
		_leftGroup.setText(NLMessages.getString("View_select_criteria_comparision"));
		_leftGroup.setLayout(new GridLayout());
		((GridLayout) _leftGroup.getLayout()).numColumns = 11;
		((GridLayout) _leftGroup.getLayout()).makeColumnsEqualWidth = false;
		_leftGroup.setLayoutData(new GridData());
		((GridData) _leftGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _leftGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _leftGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _leftGroup.getLayoutData()).grabExcessVerticalSpace = true;

		Label nameLabel = new Label(_leftGroup, SWT.NONE);
		nameLabel.setText(NLMessages.getString("View_name"));
		nameLabel.setLayoutData(new GridData());

		Button nameButton = new Button(_leftGroup, SWT.CHECK);
		nameButton.setLayoutData(new GridData());
		nameButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_compareName = !_compareName;
			}
		});

		Label birthLabel = new Label(_leftGroup, SWT.NONE);
		birthLabel.setText("Birth");
		birthLabel.setLayoutData(new GridData());
		((GridData) birthLabel.getLayoutData()).horizontalIndent = 12;
		Button birthButton = new Button(_leftGroup, SWT.CHECK);
		birthButton.setLayoutData(new GridData());
		birthButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_compareBirth = !_compareBirth;
			}
		});

		Label deathLabel = new Label(_leftGroup, SWT.NONE);
		deathLabel.setText("Death");
		deathLabel.setLayoutData(new GridData());
		((GridData) deathLabel.getLayoutData()).horizontalIndent = 12;
		Button deathButton = new Button(_leftGroup, SWT.CHECK);
		deathButton.setLayoutData(new GridData());
		deathButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_compareDeath = !_compareDeath;
			}
		});

		Label descLabel = new Label(_leftGroup, SWT.NONE);
		descLabel.setText("Description");
		descLabel.setLayoutData(new GridData());
		((GridData) descLabel.getLayoutData()).horizontalIndent = 12;
		Button descButton = new Button(_leftGroup, SWT.CHECK);
		descButton.setLayoutData(new GridData());
		descButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_compareDesc = !_compareDesc;
			}
		});

		Label fuzzyLabel = new Label(_leftGroup, SWT.NONE);
		fuzzyLabel.setText(NLMessages.getString("View_fuzzy"));
		fuzzyLabel.setLayoutData(new GridData());
		((GridData) fuzzyLabel.getLayoutData()).horizontalIndent = 12;
		Button fuzzyButton = new Button(_leftGroup, SWT.CHECK);
		fuzzyButton.setLayoutData(new GridData());
		fuzzyButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_compareFuzzy = !_compareFuzzy;
			}
		});

		_searchButton = new Button(_leftGroup, SWT.PUSH);
		_searchButton.setLayoutData(new GridData());
		_searchButton.setText(NLMessages.getString("View_search"));
		_searchButton.setEnabled(_facade.getCurrentPerson() != null);
		_searchButton.setToolTipText(NLMessages.getString("View_search_concurrences_tooltip"));
		_searchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_personQuery = new PdrQuery();
				_personQuery.setType(1);
				_personQuery.setCriterias(new Vector<Criteria>());
				Criteria c;
				Person currentPerson = _facade.getCurrentPerson();
				if (_compareName && currentPerson.getBasicPersonData() != null
						&& currentPerson.getBasicPersonData().getComplexNames() != null)
				{
					for (ComplexName cn : currentPerson.getBasicPersonData().getComplexNames())
					{
						if (cn.getForeName() != null && cn.getForeName().trim().length() > 0)
						{
							c = new Criteria();
							c.setType("tagging"); //$NON-NLS-1$
							c.setFuzzy(_compareFuzzy);
							c.setOperator("OR"); //$NON-NLS-1$
							c.setCrit0("ALL"); //$NON-NLS-1$
							c.setCrit1("ALL"); //$NON-NLS-1$
							c.setSearchText(cn.getForeName().trim());
							_personQuery.getCriterias().add(c);
						}
						if (cn.getSurName() != null && cn.getSurName().trim().length() > 0)
						{
							c = new Criteria();
							c.setType("tagging"); //$NON-NLS-1$
							c.setFuzzy(_compareFuzzy);
							c.setOperator("OR"); //$NON-NLS-1$
							c.setCrit0("ALL"); //$NON-NLS-1$
							c.setSearchText(cn.getSurName().trim());
							_personQuery.getCriterias().add(c);
						}

					}
				}
				if (_compareBirth && currentPerson.getBasicPersonData() != null
						&& currentPerson.getBasicPersonData().getBeginningOfLife() != null)
				{
					PdrDate birth = currentPerson.getBasicPersonData().getBeginningOfLife();
					c = new Criteria();
					c.setType("date"); //$NON-NLS-1$
					c.setOperator("AND"); //$NON-NLS-1$
					c.setCrit0("biographicalData"); //$NON-NLS-1$
					if (_compareFuzzy)
					{
						c.setDateFrom(new PdrDate(birth.getYear(), 1, 1));
						c.setDateTo(new PdrDate(birth.getYear(), 12, 31));
					}
					else
					{
						c.setDateFrom(new PdrDate(birth.getYear(), birth.getMonth(), 1));
						c.setDateTo(new PdrDate(birth.getYear(), birth.getMonth(), 31));
					}
					_personQuery.getCriterias().add(c);
				}
				if (_compareDeath && currentPerson.getBasicPersonData() != null
						&& currentPerson.getBasicPersonData().getEndOfLife() != null)
				{
					PdrDate death = currentPerson.getBasicPersonData().getEndOfLife();
					c = new Criteria();
					c.setType("date"); //$NON-NLS-1$
					c.setOperator("AND"); //$NON-NLS-1$
					c.setCrit0("biographicalData"); //$NON-NLS-1$
					if (_compareFuzzy)
					{
						c.setDateFrom(new PdrDate(death.getYear(), 1, 1));
						c.setDateTo(new PdrDate(death.getYear(), 12, 31));
					}
					else
					{
						c.setDateFrom(new PdrDate(death.getYear(), death.getMonth(), 1));
						c.setDateTo(new PdrDate(death.getYear(), death.getMonth(), 31));
					}
					_personQuery.getCriterias().add(c);
				}
				if (_compareDesc && currentPerson.getBasicPersonData() != null
						&& currentPerson.getBasicPersonData().getDescriptions() != null)
				{
					c = new Criteria();
					c.setType("tagging"); //$NON-NLS-1$
					c.setOperator("AND"); //$NON-NLS-1$
					c.setFuzzy(_compareFuzzy);
					c.setCrit0("principalDescription"); //$NON-NLS-1$
					String search = "";
					for (String s : currentPerson.getBasicPersonData().getDescriptions())
					{
						search += s + " ";
					}
					c.setSearchText(search);
					_personQuery.getCriterias().add(c);
				}
				_personTableViewer.setContentProvider(new PersonVectorContentProvider());
				_personTableViewer.setLabelProvider(new TableLabelProvider());
				_personTableViewer.setComparator(new PdrObjectViewComparator());

				UIJob job = new UIJob("search")
				{

					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{
						try
						{
							Vector<Person> persons = null;
							persons = _mainSearcher.searchPersons(_personQuery, monitor);
							if (persons != null && !persons.isEmpty())
							{
								for (Person p : persons)
								{
									if (p.getPdrId().equals(_facade.getCurrentPerson().getPdrId()))
									{
										persons.remove(p);
										break;
									}

								}
							}
							_personTableViewer.setInput(persons);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}

						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();

				_personTableViewer.refresh();
			}
		});

		_personTableViewer = new TableViewer(_leftGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		_personTableViewer.setContentProvider(new PersonVectorContentProvider());
		_personTableViewer.setLabelProvider(new TableLabelProvider());
		Table table = _personTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData());
		((GridData) table.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) table.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) table.getLayoutData()).horizontalSpan = 11;

		String[] titles =
		{"Name", "ID"}; //$NON-NLS-1$ //$NON-NLS-2$
		int[] bounds =
		{200, 180};

		for (int i = 0; i < titles.length; i++)
		{
			TableViewerColumn column = new TableViewerColumn(_personTableViewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			column.getColumn().addSelectionListener(getSelectionAdapter(_personTableViewer, column.getColumn(), i));

		}
		_personTableViewer.setComparator(new PdrObjectViewComparator());

		_personTableViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				ISelection selection = _personTableViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				Person selectedP = (Person) obj;
				//				           System.out.println("selected person: " + selectedP.getPdrId().toString()); //$NON-NLS-1$
				if (selectedP != null)
				{
					if (!selectedP.isAspectsLoaded())
					{
						try
						{
							_mainSearcher.searchAspectsByRelatedObject(selectedP, null);
						}
						catch (ParseException e)
						{
							e.printStackTrace();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				_facade.setConcurringPerson(selectedP);
				_mergeButtonRight.setEnabled(true);
				_mergeButtonLeft.setEnabled(true);
				_setConcurrence.setEnabled(true);
			}
		});

		_leftGroup.layout();

		_leftButtonComposite = new Composite(_leftComposite, SWT.NONE);
		_leftButtonComposite.setLayout(new GridLayout());
		((GridLayout) _leftButtonComposite.getLayout()).numColumns = 2;
		((GridLayout) _leftButtonComposite.getLayout()).makeColumnsEqualWidth = false;
		_leftButtonComposite.setLayoutData(new GridData());

		_mergeButtonLeft = new Button(_leftButtonComposite, SWT.PUSH);
		_mergeButtonLeft.setText(NLMessages.getString("View_concurrence_merge_left"));
		_mergeButtonLeft.setToolTipText(NLMessages.getString("View_concurrence_merge_left_tooltip"));
		_mergeButtonLeft.setLayoutData(new GridData());
		((GridData) _mergeButtonLeft.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		_mergeButtonLeft.setEnabled(false);
		_mergeButtonLeft.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				String message = NLMessages.getString("View_concurrence_merge_left_warning");
				message += "\n\n"; //$NON-NLS-1$
				message += NLMessages.getString("View_concurrence_merge_left_warning2");
				message += NLMessages.getString("View_lb_lb_current_person")
						+ _facade.getCurrentPerson().getDisplayName();
				message += NLMessages.getString("View_lb_lb_concurring_person")
						+ _facade.getConcurringPerson().getDisplayName();

				message += NLMessages.getString("View_lb_not_undoable");
				MessageDialog messageDialog = new MessageDialog(_parentShell, "Merge Persons Info", null, //$NON-NLS-1$
						message, MessageDialog.WARNING, new String[]
						{NLMessages.getString("View_merge"), "Cancel"}, 1);
				int returnCode = messageDialog.open();
				if (returnCode == 0)
				{
					Person p = null;
					Person oldcurrentPerson = _facade.getCurrentPerson();
					try
					{
						p = _concurrenceManager.mergePerson(_facade.getConcurringPerson(), _facade.getCurrentPerson());
					}
					catch (XMLStreamException e1)
					{
						e1.printStackTrace();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					try
					{
						_facade.savePerson(p);
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					IDBManager dbm = _facade.getDBManager();

					try
					{
						dbm.delete(oldcurrentPerson.getPdrId(), "person"); //$NON-NLS-1$
					}
					catch (Exception e)
					{
						e.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deletePersonFromAllPersons(oldcurrentPerson);
					_facade.setConcurringPerson(null);
					_facade.refreshAllData();
				}
			}

		}); // SelectionListener

		_setConcurrence = new Button(_leftButtonComposite, SWT.PUSH);
		_setConcurrence.setText(NLMessages.getString("View_concurrence_set"));
		_setConcurrence.setToolTipText(NLMessages.getString("View_concurrence_set_tooltip"));
		_setConcurrence.setLayoutData(new GridData());
		((GridData) _setConcurrence.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		_setConcurrence.setEnabled(false);
		_setConcurrence.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				Person p = _concurrenceManager.setConcurrence(_facade.getCurrentPerson(), _facade.getConcurringPerson()
						.getPdrId());
				try
				{
					_facade.savePerson(p);
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				IDBManager dbm = _facade.getDBManager();
				p = _concurrenceManager.setConcurrence(_facade.getConcurringPerson(), _facade.getCurrentPerson()
						.getPdrId());
				try
				{
					_facade.savePerson(p);
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try
				{
					dbm.saveToDB(p);
				}
				catch (XMLStreamException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		}); // SelectionListener
		_rightComposite = new Composite(_mainSashForm, SWT.NONE);
		_rightComposite.setLayout(new GridLayout());
		((GridLayout) _rightComposite.getLayout()).numColumns = 1;
		((GridLayout) _rightComposite.getLayout()).makeColumnsEqualWidth = true;
		_rightComposite.setLayoutData(new GridData());
		((GridData) _rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_rightGroup = new Group(_rightComposite, SWT.NONE);
		_rightGroup.setText(NLMessages.getString("View_similar_person"));
		_rightGroup.setLayout(new GridLayout());
		((GridLayout) _rightGroup.getLayout()).numColumns = 1;
		((GridLayout) _rightGroup.getLayout()).makeColumnsEqualWidth = true;
		_rightGroup.setLayoutData(new GridData());
		((GridData) _rightGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _rightGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightGroup.getLayoutData()).grabExcessVerticalSpace = true;
		_concurringPersonText = new Text(_rightGroup, SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		_concurringPersonText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		_concurringPersonText.setLayoutData(new GridLayout());
		_concurringPersonText.setLayoutData(new GridData());
		((GridData) _concurringPersonText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _concurringPersonText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _concurringPersonText.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _concurringPersonText.getLayoutData()).horizontalAlignment = SWT.FILL;
		_concurringPersonText.pack();
		_rightGroup.layout();
		_rightButtonComposite = new Composite(_rightComposite, SWT.NONE);
		_rightButtonComposite.setLayout(new GridLayout());
		((GridLayout) _rightButtonComposite.getLayout()).numColumns = 2;
		((GridLayout) _rightButtonComposite.getLayout()).makeColumnsEqualWidth = false;
		_rightButtonComposite.setLayoutData(new GridData());
		_mergeButtonRight = new Button(_rightButtonComposite, SWT.PUSH);
		_mergeButtonRight.setText(NLMessages.getString("View_concurrence_merge_right"));
		_mergeButtonRight.setToolTipText(NLMessages.getString("View_concurrence_merge_right_tooltip"));
		_mergeButtonRight.setLayoutData(new GridData());
		_mergeButtonRight.setEnabled(false);
		((GridData) _mergeButtonRight.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_mergeButtonRight.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				String message = NLMessages.getString("View_concurrence_merge_right_warning");
				message += "\n\n"; //$NON-NLS-1$
				message += NLMessages.getString("View_concurrence_merge_right_warning2");
				message += NLMessages.getString("View_lb_lb_current_person")
						+ _facade.getCurrentPerson().getDisplayName();
				message += NLMessages.getString("View_lb_lb_concurring_person")
						+ _facade.getConcurringPerson().getDisplayName();
				message += NLMessages.getString("View_lb_not_undoable");
				MessageDialog messageDialog = new MessageDialog(_parentShell, "Merge Persons Info", null, //$NON-NLS-1$
						message, MessageDialog.WARNING, new String[]
						{NLMessages.getString("View_merge"), "Cancel"}, 1); //$NON-NLS-2$
				int returnCode = messageDialog.open();
				if (returnCode == 0)
				{
					Person p = null;
					try
					{
						p = _concurrenceManager.mergePerson(_facade.getCurrentPerson(), _facade.getConcurringPerson());
					}
					catch (XMLStreamException e1)
					{
						e1.printStackTrace();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					try
					{
						_facade.savePerson(p);
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					IDBManager dbm = _facade.getDBManager();
					p = _facade.getConcurringPerson();
					try
					{
						dbm.delete(p.getPdrId(), "person"); //$NON-NLS-1$
					}
					catch (Exception e)
					{
						e.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deletePersonFromAllPersons(p);
					_facade.setConcurringPerson(null);
					_facade.refreshAllData();

				}
			}

		}); // SelectionListener
		_mainSashForm.setWeights(new int[]
		{1, 1});
	}

	@Override
	public final void dispose()
	{
		_facade.deleteObserver(ConcurrenceHeadView.this);

	}

	/**
	 * Gets the selection adapter.
	 * @param tableViewer the table viewer
	 * @param column the column
	 * @param index the index
	 * @return the selection adapter
	 */
	final SelectionAdapter getSelectionAdapter(final TableViewer tableViewer, final TableColumn column, final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				((PdrObjectViewComparator) tableViewer.getComparator()).setColumn(index);
				int dir = tableViewer.getTable().getSortDirection();
				if (tableViewer.getTable().getSortColumn() == column)
				{
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				}
				else
				{

					dir = SWT.DOWN;
				}
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus()
	{

	}

	// //////////////////////// Observer //////////////////////////////////////

	/**
	 * Show concurring person.
	 */
	public final void showConcurringPerson()
	{
		Person concurrP = _facade.getConcurringPerson();
		String label = ""; //$NON-NLS-1$
		if (concurrP != null)
		{
			label = NLMessages.getString("View_currently_selected_concurring_person");
			if (concurrP.getBasicPersonData() != null)
			{
				label = label + concurrP.getDisplayName(); //$NON-NLS-1$

				if (concurrP.getPdrId() != null)
				{
					label = label + "\n\nID: " + concurrP.getPdrId().toString(); //$NON-NLS-1$
				}
				if (concurrP.getBasicPersonData().getComplexNames().size() > 1)
				{
					label = label + "\n\n" + NLMessages.getString("Treeview_namevariants"); //$NON-NLS-1$ //$NON-NLS-2$
					for (int i = 1; i < concurrP.getBasicPersonData().getComplexNames().size(); i++)
					{
						label = label + concurrP.getBasicPersonData().getComplexNames().get(i).toString(); //$NON-NLS-1$
						label = label + "\n"; //$NON-NLS-1$
					}
				}
			}
		}
		else
		{
			label = NLMessages.getString("View_no_concurring_person_selected");
		}
		_concurringPersonText.setText(label);
	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus sAspect = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeView update: " + arg); //$NON-NLS-1$
		_iLogger.log(sAspect);

		if (arg.equals("newTreeObjects")) //$NON-NLS-1$
		{
			if (_facade.getCurrentTreeObjects() != null && _facade.getCurrentTreeObjects().length > 0
					&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] != null)
			{
				_searchButton.setEnabled(true);
				if (_facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] instanceof Person) //$NON-NLS-1$
				{

					_personTableViewer.setInput(null);
					_personTableViewer.refresh();
					_facade.setConcurringPerson(null);
					showConcurringPerson();
					_mergeButtonRight.setEnabled(false);
					_mergeButtonLeft.setEnabled(false);
					_setConcurrence.setEnabled(false);

				}
			}

		}
		else if (arg.equals("newConcurringPerson")) //$NON-NLS-1$
		{

			if (_facade.getConcurringPerson() != null)
			{
				showConcurringPerson();
			}
		}
		else if (arg.equals("refreshAll")) //$NON-NLS-1$
		{
			showConcurringPerson();
			_personTableViewer.setInput(null);
		}

	}
}
