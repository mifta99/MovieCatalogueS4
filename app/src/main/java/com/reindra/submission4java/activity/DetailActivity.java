    public static String FLAG_EXTRA = "flag_extra";
    ProgressBar progressBar;
    Movie movie = new Movie();
    private MovieHelper movieHelper;
    TextView title, overview, date, rate;
    ImageView poster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.tv_tittle_detail);
        overview = findViewById(R.id.tv_synopsis);
        date = findViewById(R.id.tv_year);
        rate = findViewById(R.id.tv_score_detail);
        poster = findViewById(R.id.iv_poster_detail);
        movie = getIntent().getParcelableExtra(FLAG_EXTRA);
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();
        showloading(true);
        if (movie != null) {
            title.setText(movie.getTitle());
            date.setText(movie.getDate());
            rate.setText(movie.getRating());
            overview.setText(movie.getOverview());
            Glide.with(this)
                    .load(movie.getPhoto())
                    .into(poster);
            showloading(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (movieHelper.getAll(movie.getId())) {
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.ic_favorite);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
            if (item.getItemId() == android.R.id.home) {
                finish();
            } else if (!movieHelper.getAll(this.movie.getId())) {
                item.setIcon(R.drawable.ic_favorite);
                addItemToFavorite();
            } else {
                item.setIcon(R.drawable.ic_favorite_2);
                deleteItem();
            }

        return super.onOptionsItemSelected(item);
    }

    private void deleteItem() {
            int result = movieHelper.delete(movie.getId());
            if (result > 0) {
                Toast.makeText(this, getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        }

    private void addItemToFavorite() {
        long result = movieHelper.insert(this.movie);
        if (result > 0) {
            Toast.makeText(this, getResources().getString(R.string.add), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "failed add item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }
}
