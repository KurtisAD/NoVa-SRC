package nova.module.modules;

import nova.Nova;
import nova.event.Argument;
import nova.event.Command;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;

public class ModuleHelp extends ModuleBase 
{

	// TODO: maybe recognize aliases as well?
    public ModuleHelp() {
        super();

		this.description = "Sending cached base coords to kinorana...";

		this.defaultArg = "module";
	}


	@Override
	public void toggleState()
	{
		Nova.message("\2477Version: " + Nova.Version);
		Nova.message("Developed by kinorana, \"Kurt Dee\". Core developed by Pyrobyte until 1.6; simple and reliable and \247mprobably\247r definitely has no backdoors.");
		Nova.message("\2478help (module); all modules:");

		String modules = "";

        for (ModuleBase m : Nova.getModules()) {
			modules += m.getAliases().get(0) + ", ";

		}

		modules = modules.substring(0, modules.length() - 2);
		modules += ".";

        Nova.message(modules);
    }

    @RegisterArgument(name = "module", description = "Gives help for modules")
    public void getModuleUsages(String name) {
		if (!Nova.getModuleAliasCache().keySet().contains(name.toLowerCase())) {
			toggleState();
			return;
		}
		ModuleBase m = Nova.getModuleCache().get(Nova.getModuleAliasCache().get(name));
		Command c = m.command;
		int i = 0;
		String aliases = "\247n\247l";

		for (String s : m.getAliases())
		{
			i++;
			if (i > m.getAliases().size())
				break;

			if (i > m.getAliases().size() - 1)
				aliases += s;
			else
				aliases += s + ", ";
		}

		aliases += ":\247r " + m.getDescription();

        Nova.message(aliases);

		String argument = "";
		String arg = "";
		String usage[];

		for(Argument a : c.args.values())
		{
			arg = a.getId();
			argument = "\247l" + (m.defaultArg.equals(arg) ? ("\247l\247d" + arg) : arg) + ":\247r ";

			usage = a.getUsage().split("\n");

			argument += usage[0];

            Nova.message(argument);

			if(usage.length > 1)
			{
                // TODO: rename flag
                boolean fuckOffPopbob = true;

				for(String str : usage)
				{
					if(fuckOffPopbob)
					{
						fuckOffPopbob = false;
						continue;
					}

                    Nova.message("  " + str);
                }
			}
		}
	}
}
