package org.basex.core.cmd;

import static org.basex.core.Text.*;

import org.basex.core.Command;
import org.basex.core.CommandBuilder;
import org.basex.core.Commands.Cmd;
import org.basex.core.Commands.CmdDrop;
import org.basex.core.Context;
import org.basex.core.MainProp;
import org.basex.core.User;
import org.basex.data.MetaData;
import org.basex.io.IOFile;

/**
 * Evaluates the 'drop database' command and deletes a database.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class DropDB extends Command {
  /**
   * Default constructor.
   * @param name name of database
   */
  public DropDB(final String name) {
    super(User.CREATE, name);
  }

  @Override
  protected boolean run() {
    if(!MetaData.validName(args[0], true))
      return error(NAME_INVALID_X, args[0]);

    // retrieve all databases; return true if no database is found (no error)
    final String[] dbs = databases(args[0]);
    if(dbs.length == 0) return info(NO_DB_DROPPED, args[0]);

    // loop through all databases
    boolean ok = true;
    for(final String db : dbs) {
      // close database if it's currently opened
      close(context, db);
      // check if database is still pinned
      if(context.pinned(db)) {
        info(DB_PINNED_X, db);
        ok = false;
      } else if(!drop(db, mprop)) {
        // dropping was not successful
        info(DB_NOT_DROPPED_X, db);
        ok = false;
      } else {
        info(DB_DROPPED_X, db);
      }
    }
    return ok;
  }

  /**
   * Deletes the specified database.
   * @param db database name
   * @param mprop main properties
   * @return success flag
   */
  public static synchronized boolean drop(final String db,
      final MainProp mprop) {

    final IOFile dbpath = mprop.dbpath(db);
    return dbpath.exists() && drop(dbpath, null);
  }

  /**
   * Drops a database directory.
   * @param path database path
   * @param pat file pattern
   * @return success of operation
   */
  public static synchronized boolean drop(final IOFile path, final String pat) {
    boolean ok = path.exists();
    // try to delete all files
    for(final IOFile sub : path.children()) {
      ok &= sub.isDir() ? drop(sub, pat) :
        pat != null && !sub.name().matches(pat) || sub.delete();
    }
    // only delete directory if no pattern was specified
    return (pat != null || path.delete()) && ok;
  }

  @Override
  public boolean newData(final Context ctx) {
    return close(ctx, args[0]);
  }

  @Override
  public void build(final CommandBuilder cb) {
    cb.init(Cmd.DROP + " " + CmdDrop.DB).args();
  }
}
