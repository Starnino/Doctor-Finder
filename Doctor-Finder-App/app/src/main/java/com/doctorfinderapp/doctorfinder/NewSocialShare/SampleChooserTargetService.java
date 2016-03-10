package com.doctorfinderapp.doctorfinder.NewSocialShare;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;

import java.util.ArrayList;
import java.util.List;


public class SampleChooserTargetService extends ChooserTargetService {

    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName,
                                                   IntentFilter matchedFilter) {
        ComponentName componentName = new ComponentName(getPackageName(),
                SendMessageActivity.class.getCanonicalName());
        // The list of Direct Share items. The system will show the items the way they are sorted
        // in this list.
        ArrayList<ChooserTarget> targets = new ArrayList<>();
        for (int i = 0; i < Contact.CONTACTS.length; ++i) {
            Contact contact = Contact.byId(i);
            Bundle extras = new Bundle();
            extras.putInt(Contact.ID, i);
            targets.add(new ChooserTarget(
                    // The name of this target.
                    contact.getName(),
                    // The icon to represent this target.
                    Icon.createWithResource(this, contact.getIcon()),
                    // The ranking score for this target (0.0-1.0); the system will omit items with
                    // low scores when there are too many Direct Share items.
                    0.5f,
                    // The name of the component to be launched if this target is chosen.
                    componentName,
                    // The extra values here will be merged into the Intent when this target is
                    // chosen.
                    extras));
        }
        return targets;
    }

}
