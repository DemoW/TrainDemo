package lishui.study.di

import dagger.Component
import lishui.study.TrainApp

/**
 * Definition of the Application graph
 *
 * Created by lishui.lin on 20-8-14
 */
@Component
interface AppComponent {

    fun inject(trainApp: TrainApp)
}