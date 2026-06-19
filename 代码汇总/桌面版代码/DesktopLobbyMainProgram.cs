using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Windows.Forms;

namespace KurokoBasketballDesktop
{
    /// <summary>
    /// 桌面版程序入口类。
    /// Windows 双击 exe 后会从 Main 方法开始运行。
    /// </summary>
    internal static class Program
    {
        /// <summary>
        /// 程序启动入口。
        /// 这里初始化 WinForms 设置，并打开主界面窗口。
        /// </summary>
        [STAThread]
        private static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainLobbyForm());
        }
    }

    /// <summary>
    /// 桌面版主大厅窗口。
    /// 这个类负责绘制整个桌面版主界面。
    /// </summary>
    internal sealed class MainLobbyForm : Form
    {
        private readonly Label playerNameLabel;
        private readonly PictureBox avatarBox;
        private readonly Dictionary<string, Color> avatarColors;
        private string playerName = "新晋队长";
        private Color avatarColor = Color.FromArgb(56, 189, 248);

        /// <summary>
        /// 创建主窗口并搭建全部界面区域。
        /// 包含顶部栏、快捷入口、左侧按钮、中间立绘区和底部菜单。
        /// </summary>
        public MainLobbyForm()
        {
            avatarColors = new Dictionary<string, Color>();
            avatarColors.Add("蓝色头像", Color.FromArgb(56, 189, 248));
            avatarColors.Add("红色头像", Color.FromArgb(251, 113, 133));
            avatarColors.Add("金色头像", Color.FromArgb(250, 204, 21));

            Text = "黑子的篮球游戏 - 桌面版";
            StartPosition = FormStartPosition.CenterScreen;
            MinimumSize = new Size(1100, 650);
            Size = new Size(1280, 720);
            BackColor = Color.FromArgb(17, 24, 39);
            Font = new Font("Microsoft YaHei UI", 9F, FontStyle.Regular);

            TableLayoutPanel root = new TableLayoutPanel();
            root.Dock = DockStyle.Fill;
            root.Padding = new Padding(18);
            root.BackColor = Color.FromArgb(17, 24, 39);
            root.RowCount = 4;
            root.ColumnCount = 1;
            root.RowStyles.Add(new RowStyle(SizeType.Absolute, 66));
            root.RowStyles.Add(new RowStyle(SizeType.Absolute, 56));
            root.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
            root.RowStyles.Add(new RowStyle(SizeType.Absolute, 92));
            Controls.Add(root);

            root.Controls.Add(BuildHeader(), 0, 0);
            root.Controls.Add(BuildShortcutRow(), 0, 1);
            root.Controls.Add(BuildMainArea(), 0, 2);
            root.Controls.Add(BuildBottomMenu(), 0, 3);

            playerNameLabel = FindControl<Label>("PlayerNameLabel");
            avatarBox = FindControl<PictureBox>("AvatarBox");
            RefreshPlayerProfile();
        }

        /// <summary>
        /// 创建顶部区域。
        /// 左侧是玩家头像与名字，中间是资源栏，右侧是邮件和交易市场。
        /// </summary>
        private Control BuildHeader()
        {
            TableLayoutPanel header = new TableLayoutPanel();
            header.Dock = DockStyle.Fill;
            header.ColumnCount = 3;
            header.RowCount = 1;
            header.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 190));
            header.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
            header.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 128));
            header.BackColor = Color.Transparent;

            header.Controls.Add(BuildPlayerProfile(), 0, 0);
            header.Controls.Add(BuildResourceBar(), 1, 0);
            header.Controls.Add(BuildTopRightActions(), 2, 0);
            return header;
        }

        /// <summary>
        /// 创建玩家资料区域。
        /// 头像可点击换头像，改名按钮可打开改名弹窗。
        /// </summary>
        private Control BuildPlayerProfile()
        {
            Panel panel = new Panel();
            panel.Dock = DockStyle.Fill;
            panel.BackColor = Color.Transparent;

            PictureBox avatar = new PictureBox();
            avatar.Name = "AvatarBox";
            avatar.Location = new Point(0, 7);
            avatar.Size = new Size(52, 52);
            avatar.SizeMode = PictureBoxSizeMode.CenterImage;
            avatar.Cursor = Cursors.Hand;
            avatar.Click += delegate { ShowAvatarLibrary(); };
            panel.Controls.Add(avatar);

            Label name = new Label();
            name.Name = "PlayerNameLabel";
            name.Location = new Point(62, 9);
            name.Size = new Size(110, 22);
            name.ForeColor = Color.FromArgb(229, 231, 235);
            name.Font = new Font(Font.FontFamily, 10F, FontStyle.Bold);
            panel.Controls.Add(name);

            Button renameButton = new Button();
            renameButton.Text = "改名";
            renameButton.Location = new Point(62, 35);
            renameButton.Size = new Size(58, 24);
            renameButton.FlatStyle = FlatStyle.Flat;
            renameButton.ForeColor = Color.FromArgb(250, 204, 21);
            renameButton.BackColor = Color.FromArgb(31, 41, 55);
            renameButton.FlatAppearance.BorderColor = Color.FromArgb(56, 189, 248);
            renameButton.Click += delegate { ShowRenameDialog(); };
            panel.Controls.Add(renameButton);

            return panel;
        }

        /// <summary>
        /// 创建资源栏。
        /// 按顺序展示金币、钻石、点券、黑子券。
        /// </summary>
        private Control BuildResourceBar()
        {
            FlowLayoutPanel panel = new FlowLayoutPanel();
            panel.Dock = DockStyle.Fill;
            panel.FlowDirection = FlowDirection.LeftToRight;
            panel.WrapContents = false;
            panel.Padding = new Padding(10, 11, 10, 10);
            panel.BackColor = Color.FromArgb(31, 41, 55);

            panel.Controls.Add(BuildResourceItem("金币", "12000", Color.FromArgb(250, 204, 21)));
            panel.Controls.Add(BuildResourceItem("钻石", "300", Color.FromArgb(56, 189, 248)));
            panel.Controls.Add(BuildResourceItem("点券", "88", Color.FromArgb(167, 139, 250)));
            panel.Controls.Add(BuildResourceItem("黑子券", "12", Color.FromArgb(229, 231, 235)));
            return panel;
        }

        /// <summary>
        /// 创建资源栏中的单个资源项。
        /// 图标用代码绘制，右侧显示资源数量。
        /// </summary>
        private Control BuildResourceItem(string name, string amount, Color color)
        {
            Panel item = new Panel();
            item.Width = 104;
            item.Height = 36;
            item.Margin = new Padding(4, 0, 4, 0);
            item.BackColor = Color.Transparent;

            PictureBox icon = new PictureBox();
            icon.Location = new Point(2, 5);
            icon.Size = new Size(24, 24);
            icon.Image = IconFactory.CreateCircleIcon(color, name.Substring(0, 1), 24);
            icon.SizeMode = PictureBoxSizeMode.CenterImage;
            item.Controls.Add(icon);

            Label value = new Label();
            value.Text = amount;
            value.ForeColor = Color.White;
            value.Font = new Font(Font.FontFamily, 10F, FontStyle.Bold);
            value.Location = new Point(30, 6);
            value.Size = new Size(70, 22);
            item.Controls.Add(value);

            return item;
        }

        /// <summary>
        /// 创建右上角功能入口。
        /// 目前包含邮件和篮球交易市场。
        /// </summary>
        private Control BuildTopRightActions()
        {
            FlowLayoutPanel panel = new FlowLayoutPanel();
            panel.Dock = DockStyle.Fill;
            panel.FlowDirection = FlowDirection.LeftToRight;
            panel.WrapContents = false;
            panel.Padding = new Padding(8, 8, 0, 0);
            panel.BackColor = Color.Transparent;

            panel.Controls.Add(BuildIconOnlyButton("邮件", Color.FromArgb(229, 231, 235)));
            panel.Controls.Add(BuildIconOnlyButton("篮球交易市场", Color.FromArgb(250, 204, 21)));
            return panel;
        }

        /// <summary>
        /// 创建头像下方快捷入口行。
        /// 包含会员卡商店、赛季征程、限时商店。
        /// </summary>
        private Control BuildShortcutRow()
        {
            TableLayoutPanel row = new TableLayoutPanel();
            row.Dock = DockStyle.Fill;
            row.ColumnCount = 3;
            row.RowCount = 1;
            row.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 33.33F));
            row.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 33.33F));
            row.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 33.33F));
            row.Padding = new Padding(0, 6, 0, 6);

            row.Controls.Add(BuildIconTextButton("会员卡商店", Color.FromArgb(56, 189, 248)), 0, 0);
            row.Controls.Add(BuildIconTextButton("赛季征程", Color.FromArgb(250, 204, 21)), 1, 0);
            row.Controls.Add(BuildIconTextButton("限时商店", Color.FromArgb(251, 113, 133)), 2, 0);
            return row;
        }

        /// <summary>
        /// 创建主内容区。
        /// 左边是竖排入口，右边是立绘展示区域。
        /// </summary>
        private Control BuildMainArea()
        {
            TableLayoutPanel area = new TableLayoutPanel();
            area.Dock = DockStyle.Fill;
            area.ColumnCount = 2;
            area.RowCount = 1;
            area.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 94));
            area.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
            area.BackColor = Color.Transparent;

            area.Controls.Add(BuildLeftMenu(), 0, 0);
            area.Controls.Add(BuildCharacterStandArea(), 1, 0);
            return area;
        }

        /// <summary>
        /// 创建左侧竖排功能入口。
        /// 包含扭蛋、商城、活动大厅。
        /// </summary>
        private Control BuildLeftMenu()
        {
            FlowLayoutPanel menu = new FlowLayoutPanel();
            menu.Dock = DockStyle.Fill;
            menu.FlowDirection = FlowDirection.TopDown;
            menu.WrapContents = false;
            menu.Padding = new Padding(4, 18, 8, 0);
            menu.BackColor = Color.Transparent;

            menu.Controls.Add(BuildSideEntry("扭蛋", Color.FromArgb(56, 189, 248)));
            menu.Controls.Add(BuildSideEntry("商城", Color.FromArgb(250, 204, 21)));
            menu.Controls.Add(BuildSideEntry("活动大厅", Color.FromArgb(251, 113, 133)));
            return menu;
        }

        /// <summary>
        /// 创建中间立绘展示区域。
        /// 当前只显示“立绘待补充”，后续可以替换成真实角色图。
        /// </summary>
        private Control BuildCharacterStandArea()
        {
            Panel area = new Panel();
            area.Dock = DockStyle.Fill;
            area.BackColor = Color.Transparent;

            Label placeholder = new Label();
            placeholder.Text = "立绘待补充";
            placeholder.ForeColor = Color.FromArgb(170, 229, 231, 235);
            placeholder.Font = new Font(Font.FontFamily, 18F, FontStyle.Bold);
            placeholder.TextAlign = ContentAlignment.MiddleCenter;
            placeholder.Size = new Size(260, 360);
            placeholder.Anchor = AnchorStyles.None;
            area.Controls.Add(placeholder);

            Label title = new Label();
            title.Text = "黑子的篮球游戏";
            title.ForeColor = Color.FromArgb(250, 204, 21);
            title.Font = new Font(Font.FontFamily, 16F, FontStyle.Bold);
            title.TextAlign = ContentAlignment.MiddleCenter;
            title.Size = new Size(300, 40);
            title.Anchor = AnchorStyles.None;
            area.Controls.Add(title);

            area.Resize += delegate
            {
                placeholder.Left = (area.Width - placeholder.Width) / 2;
                placeholder.Top = Math.Max(15, (area.Height - placeholder.Height) / 2 - 20);
                title.Left = (area.Width - title.Width) / 2;
                title.Top = placeholder.Bottom + 6;
            };

            return area;
        }

        /// <summary>
        /// 创建底部五个主按钮。
        /// 从左到右为潜力、篮球、背包、球员、成就。
        /// </summary>
        private Control BuildBottomMenu()
        {
            TableLayoutPanel row = new TableLayoutPanel();
            row.Dock = DockStyle.Fill;
            row.ColumnCount = 5;
            row.RowCount = 1;
            row.Padding = new Padding(0, 10, 0, 0);
            for (int i = 0; i < 5; i++)
            {
                row.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 20F));
            }

            row.Controls.Add(BuildBottomButton("潜力"), 0, 0);
            row.Controls.Add(BuildBottomButton("篮球"), 1, 0);
            row.Controls.Add(BuildBottomButton("背包"), 2, 0);
            row.Controls.Add(BuildBottomButton("球员"), 3, 0);
            row.Controls.Add(BuildBottomButton("成就"), 4, 0);
            return row;
        }

        /// <summary>
        /// 创建底部主按钮。
        /// 统一按钮高度和字体，让底部菜单风格一致。
        /// </summary>
        private Control BuildBottomButton(string text)
        {
            Button button = BuildBaseButton(text);
            button.Dock = DockStyle.Fill;
            button.Margin = new Padding(4, 0, 4, 0);
            button.Font = new Font(Font.FontFamily, 12F, FontStyle.Bold);
            return button;
        }

        /// <summary>
        /// 创建左侧单个入口。
        /// 图标在上方，功能名称在下方。
        /// </summary>
        private Control BuildSideEntry(string text, Color color)
        {
            Panel item = new Panel();
            item.Width = 74;
            item.Height = 84;
            item.Margin = new Padding(0, 0, 0, 12);
            item.Cursor = Cursors.Hand;
            item.Click += delegate { ShowFeaturePage(text); };

            PictureBox icon = new PictureBox();
            icon.Image = IconFactory.CreateSquareIcon(color, text.Substring(0, 1), 46);
            icon.Location = new Point(14, 0);
            icon.Size = new Size(46, 46);
            icon.SizeMode = PictureBoxSizeMode.CenterImage;
            icon.Click += delegate { ShowFeaturePage(text); };
            item.Controls.Add(icon);

            Label label = new Label();
            label.Text = text;
            label.ForeColor = Color.FromArgb(229, 231, 235);
            label.TextAlign = ContentAlignment.MiddleCenter;
            label.Location = new Point(0, 52);
            label.Size = new Size(74, 24);
            label.Click += delegate { ShowFeaturePage(text); };
            item.Controls.Add(label);

            return item;
        }

        /// <summary>
        /// 创建带图标和文字的横向按钮。
        /// 头像下方的三个快捷入口会用这个样式。
        /// </summary>
        private Control BuildIconTextButton(string text, Color color)
        {
            Button button = BuildBaseButton("   " + text);
            button.Dock = DockStyle.Fill;
            button.Margin = new Padding(4, 0, 4, 0);
            button.Image = IconFactory.CreateCircleIcon(color, text.Substring(0, 1), 22);
            button.ImageAlign = ContentAlignment.MiddleLeft;
            button.TextAlign = ContentAlignment.MiddleCenter;
            return button;
        }

        /// <summary>
        /// 创建只显示图标的小按钮。
        /// 右上角邮件和交易市场会用这个样式。
        /// </summary>
        private Control BuildIconOnlyButton(string text, Color color)
        {
            Button button = BuildBaseButton("");
            button.Width = 48;
            button.Height = 48;
            button.Margin = new Padding(4, 0, 4, 0);
            button.Image = IconFactory.CreateCircleIcon(color, text.Substring(0, 1), 26);
            button.Click += delegate { ShowFeaturePage(text); };
            return button;
        }

        /// <summary>
        /// 创建基础按钮。
        /// 统一按钮颜色、边框、鼠标悬停颜色和点击事件。
        /// </summary>
        private Button BuildBaseButton(string text)
        {
            Button button = new Button();
            button.Text = text;
            button.ForeColor = Color.FromArgb(229, 231, 235);
            button.BackColor = Color.FromArgb(31, 41, 55);
            button.FlatStyle = FlatStyle.Flat;
            button.FlatAppearance.BorderColor = Color.FromArgb(56, 189, 248);
            button.FlatAppearance.MouseOverBackColor = Color.FromArgb(45, 55, 72);
            button.Cursor = Cursors.Hand;
            button.Click += delegate { ShowFeaturePage(text.Trim()); };
            return button;
        }

        /// <summary>
        /// 打开改名弹窗。
        /// 用户保存后刷新左上角玩家名称。
        /// </summary>
        private void ShowRenameDialog()
        {
            using (RenameDialog dialog = new RenameDialog(playerName))
            {
                if (dialog.ShowDialog(this) == DialogResult.OK)
                {
                    playerName = dialog.PlayerName;
                    RefreshPlayerProfile();
                }
            }
        }

        /// <summary>
        /// 打开头像库弹窗。
        /// 用户选择头像后刷新左上角头像图标。
        /// </summary>
        private void ShowAvatarLibrary()
        {
            using (AvatarDialog dialog = new AvatarDialog(avatarColors))
            {
                if (dialog.ShowDialog(this) == DialogResult.OK)
                {
                    avatarColor = dialog.SelectedColor;
                    RefreshPlayerProfile();
                }
            }
        }

        /// <summary>
        /// 刷新玩家资料显示。
        /// 这个函数会同时更新昵称文字和头像图片。
        /// </summary>
        private void RefreshPlayerProfile()
        {
            if (playerNameLabel != null)
            {
                playerNameLabel.Text = playerName;
            }
            if (avatarBox != null)
            {
                Image oldImage = avatarBox.Image;
                avatarBox.Image = IconFactory.CreateAvatarIcon(avatarColor, 46);
                if (oldImage != null)
                {
                    oldImage.Dispose();
                }
            }
        }

        /// <summary>
        /// 显示功能占位提示。
        /// 当前功能页面还没真正开发，所以点击入口先弹出提示框。
        /// </summary>
        private void ShowFeaturePage(string featureName)
        {
            if (String.IsNullOrWhiteSpace(featureName))
            {
                return;
            }
            MessageBox.Show(this, featureName + " 功能页面后续开发。", featureName, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        /// <summary>
        /// 按控件名称查找指定类型控件。
        /// 用于在创建完界面后找到玩家名 Label 和头像 PictureBox。
        /// </summary>
        private T FindControl<T>(string name) where T : Control
        {
            return FindControlRecursive<T>(Controls, name);
        }

        /// <summary>
        /// 递归查找控件。
        /// 因为控件可能嵌套在多个 Panel 或 TableLayoutPanel 中，所以需要递归。
        /// </summary>
        private static T FindControlRecursive<T>(Control.ControlCollection controls, string name) where T : Control
        {
            foreach (Control control in controls)
            {
                T typed = control as T;
                if (typed != null && control.Name == name)
                {
                    return typed;
                }
                T child = FindControlRecursive<T>(control.Controls, name);
                if (child != null)
                {
                    return child;
                }
            }
            return null;
        }
    }

    /// <summary>
    /// 改名弹窗。
    /// 负责接收用户输入的新玩家名。
    /// </summary>
    internal sealed class RenameDialog : Form
    {
        private readonly TextBox input;

        /// <summary>
        /// 创建改名弹窗界面。
        /// currentName 会作为输入框的默认内容。
        /// </summary>
        public RenameDialog(string currentName)
        {
            Text = "修改玩家名称";
            StartPosition = FormStartPosition.CenterParent;
            FormBorderStyle = FormBorderStyle.FixedDialog;
            MaximizeBox = false;
            MinimizeBox = false;
            ClientSize = new Size(320, 130);
            Font = new Font("Microsoft YaHei UI", 9F);

            Label label = new Label();
            label.Text = "玩家名称";
            label.Location = new Point(18, 18);
            label.Size = new Size(80, 24);
            Controls.Add(label);

            input = new TextBox();
            input.Text = currentName;
            input.Location = new Point(100, 16);
            input.Size = new Size(190, 24);
            input.MaxLength = 8;
            Controls.Add(input);

            Button save = new Button();
            save.Text = "保存";
            save.Location = new Point(118, 78);
            save.DialogResult = DialogResult.OK;
            Controls.Add(save);

            Button cancel = new Button();
            cancel.Text = "取消";
            cancel.Location = new Point(210, 78);
            cancel.DialogResult = DialogResult.Cancel;
            Controls.Add(cancel);

            AcceptButton = save;
            CancelButton = cancel;
        }

        /// <summary>
        /// 用户输入的玩家名。
        /// 如果输入为空，就回退到默认名称。
        /// </summary>
        public string PlayerName
        {
            get
            {
                string value = input.Text.Trim();
                return value.Length == 0 ? "新晋队长" : value;
            }
        }
    }

    /// <summary>
    /// 头像选择弹窗。
    /// 当前头像库用颜色占位，后续可以替换成真实头像图片。
    /// </summary>
    internal sealed class AvatarDialog : Form
    {
        public Color SelectedColor { get; private set; }

        /// <summary>
        /// 创建头像库弹窗。
        /// avatarColors 中的每一项都会变成一个可点击头像按钮。
        /// </summary>
        public AvatarDialog(Dictionary<string, Color> avatarColors)
        {
            Text = "选择头像";
            StartPosition = FormStartPosition.CenterParent;
            FormBorderStyle = FormBorderStyle.FixedDialog;
            MaximizeBox = false;
            MinimizeBox = false;
            ClientSize = new Size(330, 135);
            Font = new Font("Microsoft YaHei UI", 9F);
            SelectedColor = Color.FromArgb(56, 189, 248);

            int left = 24;
            foreach (KeyValuePair<string, Color> pair in avatarColors)
            {
                Button button = new Button();
                button.Text = pair.Key;
                button.Image = IconFactory.CreateAvatarIcon(pair.Value, 42);
                button.TextImageRelation = TextImageRelation.ImageAboveText;
                button.Size = new Size(86, 92);
                button.Location = new Point(left, 18);
                button.Tag = pair.Value;
                button.Click += delegate(object sender, EventArgs args)
                {
                    Button clicked = (Button)sender;
                    SelectedColor = (Color)clicked.Tag;
                    DialogResult = DialogResult.OK;
                    Close();
                };
                Controls.Add(button);
                left += 96;
            }
        }
    }

    /// <summary>
    /// 图标绘制工具。
    /// 在没有真实素材时，用代码绘制头像、圆形图标和方形图标。
    /// </summary>
    internal static class IconFactory
    {
        /// <summary>
        /// 创建圆形图标。
        /// 用于资源栏和顶部小入口。
        /// </summary>
        public static Bitmap CreateCircleIcon(Color color, string text, int size)
        {
            Bitmap bitmap = new Bitmap(size, size);
            using (Graphics graphics = Graphics.FromImage(bitmap))
            {
                graphics.SmoothingMode = SmoothingMode.AntiAlias;
                using (Brush brush = new SolidBrush(color))
                {
                    graphics.FillEllipse(brush, 1, 1, size - 2, size - 2);
                }
                DrawText(graphics, text, size, Color.FromArgb(17, 24, 39));
            }
            return bitmap;
        }

        /// <summary>
        /// 创建圆角方形图标。
        /// 用于左侧扭蛋、商城、活动大厅入口。
        /// </summary>
        public static Bitmap CreateSquareIcon(Color color, string text, int size)
        {
            Bitmap bitmap = new Bitmap(size, size);
            using (Graphics graphics = Graphics.FromImage(bitmap))
            {
                graphics.SmoothingMode = SmoothingMode.AntiAlias;
                using (Brush brush = new SolidBrush(color))
                using (GraphicsPath path = RoundedRect(new Rectangle(1, 1, size - 2, size - 2), 8))
                {
                    graphics.FillPath(brush, path);
                }
                DrawText(graphics, text, size, Color.FromArgb(17, 24, 39));
            }
            return bitmap;
        }

        /// <summary>
        /// 创建头像图标。
        /// 当前头像由底色和简单人物轮廓组成。
        /// </summary>
        public static Bitmap CreateAvatarIcon(Color color, int size)
        {
            Bitmap bitmap = new Bitmap(size, size);
            using (Graphics graphics = Graphics.FromImage(bitmap))
            {
                graphics.SmoothingMode = SmoothingMode.AntiAlias;
                using (Brush brush = new SolidBrush(color))
                {
                    graphics.FillEllipse(brush, 1, 1, size - 2, size - 2);
                }
                using (Brush brush = new SolidBrush(Color.FromArgb(17, 24, 39)))
                {
                    graphics.FillEllipse(brush, size * 0.35F, size * 0.18F, size * 0.3F, size * 0.3F);
                    graphics.FillEllipse(brush, size * 0.24F, size * 0.52F, size * 0.52F, size * 0.28F);
                }
            }
            return bitmap;
        }

        /// <summary>
        /// 在图标中心绘制文字。
        /// 例如金币图标中间绘制“金”字。
        /// </summary>
        private static void DrawText(Graphics graphics, string text, int size, Color color)
        {
            using (Font font = new Font("Microsoft YaHei UI", Math.Max(8, size / 3), FontStyle.Bold))
            using (Brush brush = new SolidBrush(color))
            using (StringFormat format = new StringFormat())
            {
                format.Alignment = StringAlignment.Center;
                format.LineAlignment = StringAlignment.Center;
                graphics.DrawString(text, font, brush, new RectangleF(0, 0, size, size), format);
            }
        }

        /// <summary>
        /// 创建圆角矩形路径。
        /// 方形图标需要这个路径来绘制圆角背景。
        /// </summary>
        private static GraphicsPath RoundedRect(Rectangle bounds, int radius)
        {
            int diameter = radius * 2;
            GraphicsPath path = new GraphicsPath();
            path.AddArc(bounds.X, bounds.Y, diameter, diameter, 180, 90);
            path.AddArc(bounds.Right - diameter, bounds.Y, diameter, diameter, 270, 90);
            path.AddArc(bounds.Right - diameter, bounds.Bottom - diameter, diameter, diameter, 0, 90);
            path.AddArc(bounds.X, bounds.Bottom - diameter, diameter, diameter, 90, 90);
            path.CloseFigure();
            return path;
        }
    }
}
