package t3rmian.github.io.travis_android_demo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import t3rmian.github.io.travis_android_demo.common.ScreenshotOnTestFailedRule


class ItemListActivityTest {
    companion object {
        const val PACKAGE_NAME = "t3rmian.github.io.travis_android_demo";
    }

    @Rule
    @JvmField
    val testRule: RuleChain = RuleChain
        .outerRule(ActivityTestRule(ItemListActivity::class.java, false, false))
        .around(ScreenshotOnTestFailedRule())

    @Test
    fun testOnCreate() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.pressHome()

        val launcherPackage = getLauncherPackageName()
        Assert.assertNotNull(launcherPackage)
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000)

        val context = getApplicationContext<Context>()
        val intent = context.packageManager
            .getLaunchIntentForPackage(PACKAGE_NAME)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5000)
    }

    private fun getLauncherPackageName(): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val pm = ApplicationProvider.getApplicationContext<Context>().packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo!!.activityInfo.packageName
    }
}