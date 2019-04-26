 @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxRule = RxImmediateSchedulerRule()

    var mockServer: MockWebServer = MockWebServer()
    private val viewModel: HomeViewModel by inject()
    private val moshi = Moshi.Builder().build()

    @Before
    @Throws
    fun setUp() {
        setupKoin()
        mockServer.start()
    }

    @After
    @Throws
    fun tearDown() {
        stopKoin()
        mockServer.shutdown()
    }

    private fun setupKoin() {
        startKoin {
            modules(
                listOf(
                    interactorModule,
                    repositoryMockModule,
                    viewModelModule,
                    networkModule
                )
            )
            properties(
                mapOf(
                    PROPERTY_BASE_URL to BuildConfig.API_BASE,
                    PROPERTY_API_KEY to BuildConfig.API_KEY
                )
            )
        }
    }