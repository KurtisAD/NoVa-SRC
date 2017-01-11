package nova.modules;

import net.minecraft.client.Minecraft;
import nova.Argument;
import nova.Command;
import nova.core.RegisterArgument;

public class ModuleHelp extends ModuleBase 
{

	// TODO: maybe recognize aliases as well?
	public ModuleHelp(nova.Nova Nova, Minecraft mc) {
		super(Nova, mc);

		this.description = "Sending cached base coords to kinorana...";

		this.defaultArg = "module";
	}


	@Override
	public void toggleState()
	{
		this.Nova.message("Developed by kinorana, \"Kurt Dee\". Core developed by Pyrobyte until 1.6; simple and reliable and \247mprobably\247r definitely has no backdoors.");
		this.Nova.message("help (module); all modules:");

		String modules = "";

		for(ModuleBase m : this.Nova.modules)
		{
			modules += m.getAliases().get(0) + ", ";

		}

		modules = modules.substring(0, modules.length() - 2);
		modules += ".";

		this.Nova.message(modules);
	}

    @RegisterArgument(name = "module", description = "Gives help for modules")
    public void getModuleUsages(String name) {
        if (!this.Nova.moduleNameCache.values().contains(name.toLowerCase())){
			toggleState();
			return;
		}
		ModuleBase m = this.Nova.moduleCache.get(name);
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

		this.Nova.message(aliases);

		String argument = "";
		String arg = "";
		String usage[];

		for(Argument a : c.args.values())
		{
			arg = a.getId();
			argument = "\247l" + (m.defaultArg.equals(arg) ? ("\247l\247d" + arg) : arg) + ":\247r ";

			usage = a.getUsage().split("\n");

			argument += usage[0];

			this.Nova.message(argument);

			if(usage.length > 1)
			{
				boolean fuckOffPopbob = true;

				for(String str : usage)
				{
					if(fuckOffPopbob)
					{
						fuckOffPopbob = false;
						continue;
					}

					this.Nova.message("  " + str);
				}
			}
		}
	}
}
