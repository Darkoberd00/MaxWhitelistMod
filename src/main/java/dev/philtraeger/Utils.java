package dev.philtraeger;

import dev.philtraeger.config.ModConfigs;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Utils {

    public static boolean hasNoneRole(List<Role> userroles){
        return userroles.stream().noneMatch((role) -> role.getId().equals(ModConfigs.ADMIN_ID) ||
                role.getId().equals(ModConfigs.MOD_ID) ||
                role.getId().equals(ModConfigs.WHITELISTED_ID));
    }

    public static List<Role> getRolesFromUser(Guild guild, String userID) {
        Optional<Member> optionalMember = Optional.ofNullable(guild.retrieveMemberById(userID).complete()); // retrieve the member synchronously
        if (optionalMember.isPresent()) { // check if the member is present
            Member user = optionalMember.get();
            return user.getRoles();
        } else {
            // Handle the case where the member with the given ID is not found
            // For example, you could throw an exception or return an empty list
            return Collections.emptyList(); // returning an empty list in this example
        }
    }
}
