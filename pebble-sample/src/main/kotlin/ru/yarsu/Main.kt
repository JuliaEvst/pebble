package ru.yarsu

import io.pebbletemplates.pebble.PebbleEngine
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.DayOfWeek
import kotlin.io.path.writer

object Main {
    private const val OUTPUT_DIRECTORY = "site"

    @JvmStatic
    fun main(args: Array<String>) {

        val scheduleInfo = formSchedule()
        val pebbleEngine = configureEngine()

        // Создаём целевые каталоги
        Files.createDirectories(Paths.get(OUTPUT_DIRECTORY, "courses"))

        // Помещаем данные в шаблон "some.peb" и выводим на экран
        generateDataFromSomePeb(scheduleInfo, pebbleEngine)

        generateDataFromCourseList(scheduleInfo, pebbleEngine)

        for (i in 0 until scheduleInfo.courses.size)
            generateDataFromCourses(scheduleInfo, pebbleEngine, i)

        generateDataFromSchedule(scheduleInfo, pebbleEngine)

        generateDataFromNewCourse(scheduleInfo, pebbleEngine)

        generateDataFromNewInfoCourse(scheduleInfo, pebbleEngine)

        generateDataFromLogin(scheduleInfo, pebbleEngine)

        //printTemplateToFile(pebbleEngine, "course-list.peb", "course-list.html", emptyMap())
        //printTemplateToFile(pebbleEngine, "course.peb", "courses/course.html", emptyMap())
        //printTemplateToFile(pebbleEngine, "schedule.peb", "schedule.html", emptyMap())
    }


    private fun generateDataFromLogin(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_course: List<Course> = scheduleInfo.courses
        context["scheduleInfo"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "login.peb", "login.html", context)
    }

    private fun generateDataFromNewInfoCourse(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_idcouse: List<ScheduleItem> = scheduleInfo.scheduleItems.distinctBy { it.courseId }
        context["scheduleInfoCourse_list"] = list_idcouse

        val list_scheduleItems: List<ScheduleItem> = scheduleInfo.scheduleItems
        context["scheduleInfo_list"] = list_scheduleItems

        val list_course: List<Course> = scheduleInfo.courses
        context["scheduleInfo"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo


        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "newinfocourse.peb", "newinfocourse.html", context)
    }

    private fun generateDataFromNewCourse(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_scheduleItems: List<ScheduleItem> = scheduleInfo.scheduleItems
        context["scheduleInfo_list"] = list_scheduleItems

        val list_course: List<Course> = scheduleInfo.courses
        context["scheduleInfo"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "newcourse.peb", "newcourse.html", context)
    }

    private fun generateDataFromSchedule(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_scheduleItems: List<ScheduleItem> = scheduleInfo.scheduleItems.sortedWith(compareBy({it.dayOfWeek}, {it.startTime}))
        context["scheduleItems"] = list_scheduleItems

        val list_course: List<Course> = scheduleInfo.courses
        context["courses"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "schedule.peb", "schedule.html", context)
    }

    private fun generateDataFromCourses(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine,
        i: Int
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_scheduleItems: List<ScheduleItem> = scheduleInfo.scheduleItems.sortedWith(compareBy({it.dayOfWeek}, {it.startTime}))
        context["scheduleItems"] = list_scheduleItems

        val list_course: List<Course> = scheduleInfo.courses
        context["scheduleInfo"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses[i]
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "course.peb", "courses/${course.id}.html", context)
    }

    private fun generateDataFromCourseList(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        //Cписок всех курсов
        val courses: List<Course> = scheduleInfo.courses
        context["courses"] = courses

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "course-list.peb", "course-list.html", context)
    }

    private fun generateDataFromSomePeb(
        scheduleInfo: ScheduleInfo,
        pebbleEngine: PebbleEngine
    ) {
        // Формируем контекст, который необходим шаблону для отображения данных
        val context = mutableMapOf<String, Any>()

        val list_scheduleItems: List<ScheduleItem> = scheduleInfo.scheduleItems
        context["scheduleInfo_list"] = list_scheduleItems

        val list_pr: List<Course> = scheduleInfo.courses.sortedBy { it.lecturerFullName }
        context["list_pr"] = list_pr

        val list_course_pr: List<Course> = scheduleInfo.courses.distinctBy { it.lecturerFullName }
        context["list_distinct_pr"] = list_course_pr

        val list_course: List<Course> = scheduleInfo.courses
        context["scheduleInfo"] = list_course

        // Первый элемент из списка расписаний
        val itemInfo = scheduleInfo.scheduleItems.first()
        context["scheduleItem"] = itemInfo

        // Находим соответствующий ему курс по идентификатору
        val course = scheduleInfo.courses
            .first { it.id == itemInfo.courseId }
        context["course"] = course

        context["Monday"] = DayOfWeek.MONDAY
        context["Tuesday"] = DayOfWeek.TUESDAY
        context["Wednesday"] = DayOfWeek.WEDNESDAY
        context["Thursday"] = DayOfWeek.THURSDAY
        context["Friday"] = DayOfWeek.FRIDAY
        context["Saturday"] = DayOfWeek.SATURDAY
        context["Sunday"] = DayOfWeek.SUNDAY

        // Выводим результат в файл some.html
        printTemplateToFile(pebbleEngine, "some.peb", "some.html", context)
    }
    private fun configureEngine(): PebbleEngine {
        return PebbleEngine.Builder().build()
    }

    private fun printTemplateToFile(
        pebbleEngine: PebbleEngine,
        templateName: String,
        fileName: String,
        context: Map<String, Any>,
    ) {
        val template = pebbleEngine.getTemplate("ru/yarsu/${templateName}")
        val fileWriter = Paths.get(OUTPUT_DIRECTORY, fileName).writer()
        try {
            template.evaluate(fileWriter, context)
        } catch (exception: IOException) {
            System.err.apply {
                println("Не могу обработать шаблон $templateName")
                println(exception.message)
            }
        }
    }
}