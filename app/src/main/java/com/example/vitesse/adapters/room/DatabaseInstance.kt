import android.content.Context
import androidx.room.Room
import com.example.vitesse.adapters.room.AppDatabase
import com.example.vitesse.adapters.room.DatabaseMigrations
import com.example.vitesse.adapters.room.DatabaseMigrations.MIGRATION_1_2

object DatabaseInstance {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
