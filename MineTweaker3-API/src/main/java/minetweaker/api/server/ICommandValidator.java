/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.server;

import minetweaker.api.player.IPlayer;
import org.openzen.zencode.annotations.ZenClass;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.server.ICommandValidator")
public interface ICommandValidator {
	public boolean canExecute(IPlayer player);
}